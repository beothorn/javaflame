package com.github.beothorn.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.not;

public class MethodInstrumentationAgent {

    private static File snapshotDirectory;

    public static void premain(
        String argumentParameter,
        Instrumentation instrumentation
    ) {
        System.out.println("[JAVA_AGENT] LOG Agent loaded");
        String argument = argumentParameter == null ? "" : argumentParameter;
        boolean detailed = argumentHasDetailedMode(argument);
        SpanCatcher.debug = argumentHasDebugMode(argument);
        Optional<String> maybeFilePath = outputFileOnArgument(argument);

        Optional<File> file = maybeFilePath
                .map(File::new)
                .filter(f -> f.exists() || f.isDirectory());

        if(maybeFilePath.isPresent() && file.isEmpty()){
            System.err.println("[JAVA_AGENT] ERROR Bad directory: '"+maybeFilePath.get()+"'");
            System.err.println("[JAVA_AGENT] ERROR Directory needs to exist!");
            System.err.println("[JAVA_AGENT] ERROR Will use temporary instead");
        }

        File javaFlameDirectory;
        try {
            javaFlameDirectory = file.orElse(Files.createTempDirectory("javaflame").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            snapshotDirectory = new File(javaFlameDirectory.getAbsolutePath(), System.currentTimeMillis() + "_snap");
            if(!snapshotDirectory.mkdir()){
                System.err.println("[JAVA_AGENT] Could not create dir "+snapshotDirectory.getAbsolutePath());
            }
            extractFromResources(snapshotDirectory, "index.html");
            extractFromResources(snapshotDirectory, "d3.v7.js");
            extractFromResources(snapshotDirectory, "d3-flamegraph.css");
            extractFromResources(snapshotDirectory, "d3-flamegraph.min.js");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            File dataFile = new File(snapshotDirectory.getAbsolutePath(), "data.js");
            try (FileWriter fw = new FileWriter(dataFile)){
                fw.write("var data = "+SpanCatcher.getFinalCallStack());
                fw.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("[JAVA_AGENT] LOG Flamegraph output to '"+snapshotDirectory.getAbsolutePath()+"'");
        }));

        List<String> excludes = argumentExcludes(argument);
        List<String> filters = argumentFilter(argument);
        System.out.println("[JAVA_AGENT] LOG Modes: debug:"+SpanCatcher.debug+" detailed:"+detailed+
                " output to '"+snapshotDirectory.getAbsolutePath()+"' " +
                "excludes:"+Arrays.toString(excludes.toArray())+" filters:"+Arrays.toString(filters.toArray()));
        Advice advice = detailed ? Advice.to(SpanCatcherDetailed.class) : Advice.to(SpanCatcher.class);
        ElementMatcher.Junction<TypeDescription> exclusions = nameContains("com.github.beothorn.agent");
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

        AgentBuilder.Identified.Narrowable withoutExtraExcludes = new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(new DebugListener(SpanCatcher.debug))
                .type(withExclusions);
        withoutExtraExcludes
            .transform(new AgentBuilder.Transformer() {
                public DynamicType.Builder<?> transform(
                        DynamicType.Builder<?> builder,
                        TypeDescription typeDescription,
                        ClassLoader classLoader,
                        JavaModule module
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
                    if(SpanCatcher.debug){
                        System.out.println("[JAVA_AGENT] LOG Transform '"+ typeDescription.getCanonicalName()+"'");
                    }
                    return builder.visit(advice.on(ElementMatchers.isMethod()));
                }
            })
            .installOn(instrumentation);
    }

    public static boolean argumentHasDetailedMode(String argument){
        return argument.contains("mode:detailed");
    }

    public static boolean argumentHasDebugMode(String argument){
        return argument.contains("mode:debug");
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

        private final boolean debug;

        DebugListener(boolean debug){
            this.debug = debug;
        }

        @Override
        public void onDiscovery(
                String typeName,
                ClassLoader classLoader,
                JavaModule module,
                boolean loaded
        ) {
            if(debug) System.out.println("[JAVA_AGENT] LOG onDiscovery(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
            if(debug) System.out.println("[JAVA_AGENT] LOG onTransformation(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "DynamicType dynamicType='"+dynamicType+"')");
        }

        @Override
        public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
            if(debug) System.out.println("[JAVA_AGENT] LOG onIgnored(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }

        @Override
        public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            System.err.println("[JAVA_AGENT] ERROR onError(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "Throwable throwable='"+throwable+"')");
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            if(debug) System.out.println("[JAVA_AGENT] LOG onComplete(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }
    }
}