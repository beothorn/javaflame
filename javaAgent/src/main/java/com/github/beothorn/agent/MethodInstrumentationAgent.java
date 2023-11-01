package com.github.beothorn.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.beothorn.agent.MethodInstrumentationAgent.Flag.*;
import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.*;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class MethodInstrumentationAgent {

    private static File snapshotDirectory;

    private static final long SAVE_SNAPSHOT_INTERVAL_MILLIS = 1000L;

    static ReentrantLock fileWriteLock = new ReentrantLock();

    public enum Flag{

        NO_CAPTURING_VALUES("no_capturing_values"),
        CORE_CLASSES("core_classes"),
        NO_CONSTRUCTOR("no_constructor"),
        NO_SNAPSHOTS("no_snapshots"),
        QUALIFIED_FUNCTIONS("qualified_functions");

        public final String flagAsString;

        Flag(String flagAsString) {
            this.flagAsString = flagAsString;
        }

        public boolean isOnArguments(String arguments){
            return Pattern.compile("(^|,)" + this.flagAsString + "(,|$)")
                    .matcher(arguments).find();
        }

        public static String[] allFlagsOnArgument(String arguments){
            return Arrays.stream(Flag.values())
                    .filter(f -> f.isOnArguments(arguments))
                    .map(f -> f.flagAsString)
                    .toArray(String[]::new);
        }
    }

    public enum LogLevel{
        NONE(1),
        ERROR(2),
        INFO(3),
        WARN(4),
        DEBUG(5);

        private final Integer level;

        LogLevel(int level) {
            this.level = level;
        }

        public boolean shouldPrint(LogLevel level) {
            return level.level <= this.level ;
        }
    }

    private static LogLevel currentLevel = ERROR;

    public static void log(LogLevel level, String log){
        if(currentLevel.shouldPrint(level)){
            if(level.equals(ERROR)){
                System.err.println("[JAVA_AGENT] "+level.name()+" "+log);
            }else{
                System.out.println("[JAVA_AGENT] "+level.name()+" "+log);
            }
        }
    }

    public static void premain(
        String argumentParameter,
        Instrumentation instrumentation
    ) {
        String argument = argumentParameter == null ? "" : argumentParameter;

        currentLevel = argumentLogLevel(argument);
        log(INFO, "Agent loaded");
        boolean shouldCaptureValues = !argumentHasNoCaptureValuesMode(argument);
        Optional<String> maybeFilePath = outputFileOnArgument(argument);

        Optional<File> file = maybeFilePath
                .map(File::new)
                .filter(f -> f.exists() || f.isDirectory());

        if(maybeFilePath.isPresent() && file.isEmpty()){
            log(ERROR, "Bad directory: '"+maybeFilePath.get()+"'");
            log(ERROR, "Directory needs to exist!");
            log(ERROR, "Will use temporary instead");
        }

        File javaFlameDirectory;
        try {
            javaFlameDirectory = file.orElse(Files.createTempDirectory("javaflame").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log(INFO, "Output at "+javaFlameDirectory.getAbsolutePath());

        writeHtmlFiles(javaFlameDirectory);

        List<String> excludes = argumentExcludes(argument);
        List<String> filters = argumentFilter(argument);

        String executionMetadata = "logLevel :" + currentLevel.name()
                + " flags:" + Arrays.toString(allFlagsOnArgument(argument))
                + " output to '" + snapshotDirectory.getAbsolutePath() + "'"
                + " excludes:" + Arrays.toString(excludes.toArray())
                + " filters:" + Arrays.toString(filters.toArray());
        log(DEBUG, executionMetadata);

        String executionMetadataFormatted = "<p>Flags: " + Arrays.toString(allFlagsOnArgument(argument)) + "</p>"
                + "<p>Output: '" + snapshotDirectory.getAbsolutePath() + "'</p>"
                + "<p>Excludes: " + Arrays.toString(excludes.toArray()) + "</p>"
                + "<p>Filters: " + Arrays.toString(filters.toArray()) + "</p>";

        ElementMatcher.Junction<TypeDescription> argumentsMatcher = getMatcherFromArguments(excludes, filters);

        AgentBuilder agentBuilder = new AgentBuilder.Default();

        boolean coreClassesMode = argumentHasIncludeCoreClasses(argument);
        if(coreClassesMode){
            agentBuilder = agentBuilder.ignore(none());
        }

        boolean noConstructorMode = argumentHasNoConstructorMode(argument);
        Advice advice = shouldCaptureValues ? Advice.to(FunctionCallRecorderWithValueCapturing.class) : Advice.to(FunctionCallRecorder.class);
        AgentBuilder.Identified.Narrowable withoutExtraExcludes = agentBuilder
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(new DebugListener())
                .type(argumentsMatcher);
        withoutExtraExcludes
            .transform(new IntroduceMethodInterception(noConstructorMode, advice))
            .installOn(instrumentation);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            writeSnapshotToFile(executionMetadataFormatted);
        }));

        Thread snapshotThread = new Thread(() -> {
            while (true) {
                writeSnapshotToFile(executionMetadataFormatted);
                try {
                    Thread.sleep(SAVE_SNAPSHOT_INTERVAL_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        if(!argumentHasNoSnapshotsMode(argument)){
            snapshotThread.setDaemon(true);
            snapshotThread.start();
        }
    }

    private static void writeSnapshotToFile(String executionMetadataFormatted) {
        fileWriteLock.lock();
        try {
            FunctionCallRecorder.getOldCallStack().ifPresent(oldCallStack -> {
                try {
                    File dataFile = new File(snapshotDirectory.getAbsolutePath(), "data.js");
                    if (dataFile.exists()) {
                        RandomAccessFile raf = new RandomAccessFile(dataFile, "rw");
                        long length = raf.length();
                        long pos = length - 3; // 3 bytes = \n];
                        raf.seek(pos);
                        raf.writeBytes("\n" + oldCallStack + ",\n];");
                        raf.close();
                    } else {
                        try (FileWriter fw = new FileWriter(dataFile)) {
                            String content = "var executionMetadata = \""+ executionMetadataFormatted +"\";\n" +
                                    "var data = [" + oldCallStack + ",\n];";
                            fw.write(content);
                            fw.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    log(INFO, "Snapshot '" + dataFile.getAbsolutePath() + "'");
                    log(INFO, "Graph at '" + dataFile.getParentFile().getAbsolutePath() + "/index.html'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } finally {
            fileWriteLock.unlock();
        }
    }

    private static ElementMatcher.Junction<TypeDescription> getMatcherFromArguments(
            List<String> excludes,
            List<String> filters
    ) {
        ElementMatcher.Junction<TypeDescription> exclusions = nameContains("com.github.beothorn.agent")
                .or(nameContains("net.bytebuddy"));
        for (String exclude: excludes) {
            exclusions = exclusions.or(nameContains(exclude));
        }
        ElementMatcher.Junction<TypeDescription> withExclusions = not(exclusions);

        if(!filters.isEmpty()){
            ElementMatcher.Junction<NamedElement> namedElementJunction = nameContains(filters.get(0));
            for (int i = 1; i < filters.size(); i++) {
                namedElementJunction = namedElementJunction.or(nameContains(filters.get(i)));
            }
            withExclusions = withExclusions.and(namedElementJunction);
        }
        return withExclusions;
    }

    private static void writeHtmlFiles(File javaFlameDirectory) {
        try {
            snapshotDirectory = new File(javaFlameDirectory.getAbsolutePath(), System.currentTimeMillis() + "_snap");
            if(!snapshotDirectory.mkdir()){
                log(ERROR, "Could not create dir "+snapshotDirectory.getAbsolutePath());
            }
            extractFromResources(snapshotDirectory, "index.html");
            extractFromResources(snapshotDirectory, "code.js");
            extractFromResources(snapshotDirectory, "style.css");
            extractFromResources(snapshotDirectory, "logo.svg");
            extractFromResources(snapshotDirectory, "d3.v7.js");
            extractFromResources(snapshotDirectory, "d3-flamegraph.css");
            extractFromResources(snapshotDirectory, "d3-flamegraph.min.js");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean argumentHasQualifiedFunctions(String argument){
        return QUALIFIED_FUNCTIONS.isOnArguments(argument);
    }

    public static boolean argumentHasNoCaptureValuesMode(String argument){
        return NO_CAPTURING_VALUES.isOnArguments(argument);
    }

    public static boolean argumentHasIncludeCoreClasses(String argument){
        return CORE_CLASSES.isOnArguments(argument);
    }

    public static boolean argumentHasNoConstructorMode(String argument){
        return NO_CONSTRUCTOR.isOnArguments(argument);
    }

    public static boolean argumentHasNoSnapshotsMode(String argument){
        return NO_SNAPSHOTS.isOnArguments(argument);
    }

    public static List<String> argumentExcludes(String argument){
        Matcher matcher = Pattern.compile("exclude:([^,]+)")
                .matcher(argument);

        return getAllStringsForMatcher(matcher);
    }

    public static List<String> argumentFilter(String argument){
        Matcher matcher = Pattern.compile("filter:([^,]+)")
                .matcher(argument);

        return getAllStringsForMatcher(matcher);
    }

    public static LogLevel argumentLogLevel(String argument){
        Matcher matcher = Pattern.compile("log:([^,]+)")
                .matcher(argument);

        if(matcher.find()) {
            return LogLevel.valueOf(matcher.group(1));
        }

        return INFO;
    }

    private static ArrayList<String> getAllStringsForMatcher(Matcher matcher) {
        ArrayList<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    public static Optional<String> outputFileOnArgument(String argument){
        if(!argument.contains("out:")){
            return Optional.empty();
        }
        String afterOut = argument.split("out:")[1];
        int separator = afterOut.indexOf(',');
        if(separator == -1){
           return Optional.of(afterOut);
        }
        String filePath = afterOut.substring(0, separator);
        return Optional.of(filePath);
    }

    private static void extractFromResources(final File snapshotDirectory, final String fileToBeExtracted) throws IOException {
        File indexHtmlOut = new File(snapshotDirectory.getAbsolutePath(), fileToBeExtracted);
        try(InputStream in = MethodInstrumentationAgent.class.getResourceAsStream(fileToBeExtracted)){
            if(in == null){
                throw new RuntimeException("[JAVA_AGENT] ERROR Jar is corrupted, missing file '"+ fileToBeExtracted +"'");
            }
            try(FileOutputStream out = new FileOutputStream(indexHtmlOut)){
                out.write(in.readAllBytes());
            }
        }
    }

    private static class DebugListener implements AgentBuilder.Listener {

        @Override
        public void onDiscovery(
                String typeName,
                ClassLoader classLoader,
                JavaModule module,
                boolean loaded
        ) {
            log(DEBUG, "onDiscovery(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
            log(DEBUG, "onTransformation(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "DynamicType dynamicType='"+dynamicType+"')");
        }

        @Override
        public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
            log(DEBUG, "onIgnored(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }

        @Override
        public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            log(WARN, "onError(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "Throwable throwable='"+throwable+"')");
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            log(DEBUG, "onComplete(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }
    }

    private static class IntroduceMethodInterception implements AgentBuilder.Transformer {
        private final boolean noConstructorMode;
        private final Advice advice;

        public IntroduceMethodInterception(boolean noConstructorMode, Advice advice) {
            this.noConstructorMode = noConstructorMode;
            this.advice = advice;
        }

        public DynamicType.Builder<?> transform(
                DynamicType.Builder<?> builder,
                TypeDescription typeDescription,
                ClassLoader ignoredClassLoader,
                JavaModule ignoredModule
        ) {
            return getBuilder(builder, typeDescription);
        }

        @Override
        public DynamicType.Builder<?> transform(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain
        ) {
            return getBuilder(builder, typeDescription);
        }

        private DynamicType.Builder<?> getBuilder(DynamicType.Builder<?> builder, TypeDescription typeDescription) {
            log(DEBUG, "Transform '"+ typeDescription.getCanonicalName()+"'");
            ElementMatcher.Junction<MethodDescription> matcher = isMethod();
            if(noConstructorMode){
                matcher = matcher.and(not(isConstructor()));
            }
            return builder.visit(advice.on(matcher));
        }
    }
}