package com.github.beothorn.agent;

import com.github.beothorn.agent.parser.ClassAndMethodMatcher;
import com.github.beothorn.agent.parser.CompilationException;
import com.github.beothorn.agent.parser.ElementMatcherFromExpression;
import com.github.beothorn.agent.parser.advice.AdviceConstructorCallRecorder;
import com.github.beothorn.agent.parser.advice.AdviceConstructorCallRecorderWithCapture;
import com.github.beothorn.agent.parser.advice.AdviceFunctionCallRecorder;
import com.github.beothorn.agent.parser.advice.AdviceFunctionCallRecorderWithCapture;
import com.github.beothorn.agent.recorder.FunctionCallRecorder;
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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.beothorn.agent.MethodInstrumentationAgent.Flag.*;
import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.*;
import static com.github.beothorn.agent.parser.ElementMatcherFromExpression.forExpression;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class MethodInstrumentationAgent {

    private static File snapshotDirectory;

    private static final long SAVE_SNAPSHOT_INTERVAL_MILLIS = 1000L;

    private static final ReentrantLock fileWriteLock = new ReentrantLock();

    private static final Set<String> debugTransformedClasses = new HashSet<>();
    private static final Set<String> debugDiscoveredClasses = new HashSet<>();
    private static final Set<String> debugIgnoredClasses = new HashSet<>();
    private static final Set<String> debugErrorClasses = new HashSet<>();
    private static final Set<String> debugCompletedClasses = new HashSet<>();

    public enum Flag{

        NO_CAPTURING_VALUES("no_capturing_values"),
        CORE_CLASSES("core_classes"),
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

    public static LogLevel currentLevel = ERROR;

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

        if(maybeFilePath.isPresent() && !file.isPresent()){
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

        snapshotDirectory = new File(javaFlameDirectory.getAbsolutePath(), System.currentTimeMillis() + "_snap");
        log(INFO, "Output at "+snapshotDirectory.getAbsolutePath());
        if(!snapshotDirectory.mkdir()){
            log(ERROR, "Could not create dir "+snapshotDirectory.getAbsolutePath());
        }

        writeHtmlFiles();

        Optional<String> filter = argumentFilter(argument);
        Optional<String> maybeStartRecordingTriggerFunction = argumentStartRecordingTriggerFunction(argument);
        Optional<String> maybeStopRecordingTriggerFunction = argumentStopRecordingTriggerFunction(argument);

        String[] allFlags = allFlagsOnArgument(argument);
        String allFlagsAsString = Arrays.toString(allFlags);
        String outputDirectory = snapshotDirectory.getAbsolutePath();

        String executionMetadata = "logLevel :" + currentLevel.name()
                + " flags:" + allFlagsAsString
                + " output to '" + outputDirectory + "'"
                + " filters:" + filter.orElse("No filter parameter")
                + maybeStartRecordingTriggerFunction.map(s -> "Start recording trigger function" + s).orElse("")
                + maybeStopRecordingTriggerFunction.map(s -> "Stop recording trigger function" + s).orElse("");
        log(DEBUG, executionMetadata);

        String executionMetadataFormatted = getExecutionMetadataAsHtml(
            allFlagsAsString,
            outputDirectory,
            filter,
            maybeStartRecordingTriggerFunction,
            maybeStopRecordingTriggerFunction
        );

        maybeStartRecordingTriggerFunction.ifPresent(FunctionCallRecorder::setStartTrigger);
        maybeStopRecordingTriggerFunction.ifPresent(FunctionCallRecorder::setStopTrigger);

        ElementMatcher.Junction<NamedElement> exclusion = not(
            nameContains("com.github.beothorn.agent").or(nameContains("net.bytebuddy"))
        );

        Optional<ElementMatcherFromExpression> elementMatcherFromExpression = filter.map(f -> {
            try {
                return forExpression(f);
            } catch (CompilationException e) {
                throw new RuntimeException(e);
            }
        });

        ElementMatcher.Junction<NamedElement> argumentsMatcher = elementMatcherFromExpression
                .map(m -> exclusion.and(m.getClassMatcher()))
                .orElse(exclusion);

        AgentBuilder agentBuilder = new AgentBuilder.Default();

        boolean coreClassesMode = argumentHasIncludeCoreClasses(argument);
        if(coreClassesMode){
            agentBuilder = agentBuilder.ignore(none());
        }

        Advice adviceForFunction;
        Advice adviceForConstructor;
        if (shouldCaptureValues){
            adviceForFunction = Advice.to(AdviceFunctionCallRecorderWithCapture.class);
            adviceForConstructor = Advice.to(AdviceConstructorCallRecorderWithCapture.class);
        } else {
            adviceForFunction = Advice.to(AdviceFunctionCallRecorder.class);
            adviceForConstructor = Advice.to(AdviceConstructorCallRecorder.class);
        }

        List<ClassAndMethodMatcher> classAndMethodMatchers = elementMatcherFromExpression
                .map(ElementMatcherFromExpression::getClassAndMethodMatchers)
                .orElse(new ArrayList<>());

        agentBuilder
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(new DebugListener())
                .type(argumentsMatcher)
            .transform(new IntroduceMethodInterception(
                adviceForFunction,
                adviceForConstructor,
                classAndMethodMatchers
            ))
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

    public static String getExecutionMetadataAsHtml(
        String allFlagsAsString,
        String outputDirectory,
        Optional<String> filter,
        Optional<String> maybeStartRecordingTriggerFunction,
        Optional<String> maybeStopRecordingTriggerFunction
    ) {
        return "<p>Flags: " + allFlagsAsString + "</p>"
                + "<p>Output: '" + outputDirectory + "'</p>"
                + "<p>Filters: " + filter.orElse("No filter parameter") + "</p>"
                + maybeStartRecordingTriggerFunction.map(s -> "<p>Start recording trigger function: '" + s + "'</p>").orElse("")
                + maybeStopRecordingTriggerFunction.map(s -> "<p>Stop recording trigger function: '" + s + "'</p>").orElse("");
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
                    log(INFO, "Graph at '" + dataFile.getParentFile().getAbsolutePath()
                            + FileSystems.getDefault().getSeparator() + "index.html'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if (DEBUG.equals(currentLevel)) {
                writeDebugFile(debugTransformedClasses, "debugTransformedClasses.txt");
                writeDebugFile(debugDiscoveredClasses, "debugDiscoveredClasses.txt");
                writeDebugFile(debugIgnoredClasses, "debugIgnoredClasses.txt");
                writeDebugFile(debugErrorClasses, "debugErrorClasses.txt");
                writeDebugFile(debugCompletedClasses, "debugCompletedClasses.txt");
            }
        } finally {
            fileWriteLock.unlock();
        }
    }

    private static void writeDebugFile(final Set<String> debugClassesToWrite, final String file) {
        if (!debugClassesToWrite.isEmpty()){
            try {
                File debugLogFile = new File(snapshotDirectory.getAbsolutePath(), file);
                if (debugLogFile.exists()) {
                    RandomAccessFile raf = new RandomAccessFile(debugLogFile, "rw");
                    for (String className: debugClassesToWrite) {
                        raf.writeBytes(className + "\n");
                    }
                    raf.close();
                } else {
                    try (FileWriter fw = new FileWriter(debugLogFile)) {
                        for (String className: debugClassesToWrite) {
                            fw.write(className + "\n");
                        }
                        fw.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                debugClassesToWrite.clear();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void writeHtmlFiles() {
        try {
            extractFromResources(snapshotDirectory, "index.html");
            extractFromResources(snapshotDirectory, "code.js");
            extractFromResources(snapshotDirectory, "ui.js");
            extractFromResources(snapshotDirectory, "style.css");
            extractFromResources(snapshotDirectory, "logo.svg");
            extractFromResources(snapshotDirectory, "stackignite.js");
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

    public static boolean argumentHasNoSnapshotsMode(String argument){
        return NO_SNAPSHOTS.isOnArguments(argument);
    }

    public static Optional<String> argumentFilter(String argument){
        return matchCommand(
            argument,
            "filter"
        );
    }

    private static Optional<String> matchCommand(
        String argument,
        String command
    ) {
        Matcher matcher = Pattern.compile(command + ":([^,]+)")
                .matcher(argument);
        if(!matcher.find()) return Optional.empty();
        return Optional.of(matcher.group(1));
    }

    public static Optional<String> argumentStartRecordingTriggerFunction(String argument){
        Matcher matcher = Pattern.compile("startRecordingTriggerFunction:([^,]+)")
                .matcher(argument);
        if(!matcher.find()) return Optional.empty();
        return Optional.of(matcher.group(1));
    }

    public static Optional<String> argumentStopRecordingTriggerFunction(String argument){
        Matcher matcher = Pattern.compile("stopRecordingTriggerFunction:([^,]+)")
                .matcher(argument);
        if(!matcher.find()) return Optional.empty();
        return Optional.of(matcher.group(1));
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

    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 4 * 0x400; // 4KB
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;

        try {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                    outputStream.write(buf, 0, readLen);

                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
            }
        }
    }

    private static void extractFromResources(final File snapshotDirectory, final String fileToBeExtracted) throws IOException {
        File indexHtmlOut = new File(snapshotDirectory.getAbsolutePath(), fileToBeExtracted);
        try(InputStream in = MethodInstrumentationAgent.class.getResourceAsStream(fileToBeExtracted)){
            if(in == null){
                throw new RuntimeException("[JAVA_AGENT] ERROR Jar is corrupted, missing file '"+ fileToBeExtracted +"'");
            }
            try(FileOutputStream out = new FileOutputStream(indexHtmlOut)){
                out.write(readAllBytes(in));
            }
        }
    }

    public static class DebugListener implements AgentBuilder.Listener {

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
            debugDiscoveredClasses.add(typeName);
        }

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
            debugTransformedClasses.add(typeDescription.getCanonicalName());
            log(DEBUG, "onTransformation(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "DynamicType dynamicType='"+dynamicType+"')");
        }

        @Override
        public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
            debugIgnoredClasses.add(typeDescription.getCanonicalName());
            log(DEBUG, "onIgnored(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }

        @Override
        public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            debugErrorClasses.add(typeName);
            throwable.printStackTrace();
            log(ERROR, "onError(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "Throwable throwable='"+throwable+"')");
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            debugCompletedClasses.add(typeName);
            log(DEBUG, "onComplete(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }
    }

    private static class IntroduceMethodInterception implements AgentBuilder.Transformer {
        private final Advice adviceForFunction;
        private final Advice adviceForConstructor;
        private final List<ClassAndMethodMatcher> filters;

        public IntroduceMethodInterception(
            Advice adviceForFunction,
            Advice adviceForConstructor,
            List<ClassAndMethodMatcher> filters
        ) {
            this.adviceForFunction = adviceForFunction;
            this.adviceForConstructor = adviceForConstructor;
            this.filters = filters;
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

        private DynamicType.Builder<?> getBuilder(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription
        ) {
            String canonicalName = typeDescription.getCanonicalName();
            log(DEBUG, "Transform '"+ canonicalName +"'");

            ElementMatcher.Junction<MethodDescription> funMatcherMethod = isMethod();

            for (final ClassAndMethodMatcher classAndMethodFilter : filters) {
                if (classAndMethodFilter.classMatcher.matches(typeDescription)) {
                    funMatcherMethod = funMatcherMethod.and(classAndMethodFilter.methodMatcher);
                    log(DEBUG, "With match: ["+canonicalName+"]: "+funMatcherMethod);
                    return builder.visit(adviceForFunction.on(funMatcherMethod));
                }
            }
            log(DEBUG, "With NO match: ["+canonicalName+"]: "+funMatcherMethod);

            return builder.visit(adviceForConstructor.on(isConstructor()))
                    .visit(adviceForFunction.on(funMatcherMethod));
        }
    }
}