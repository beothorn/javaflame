package com.github.beothorn.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.not;

public class MethodInstrumentationAgent {

    private static File snapshotDirectory;

    public static void premain(
        String argumentParameter,
        Instrumentation instrumentation
    ) {
        System.out.println("[JAVA_AGENT] Agent loaded");
        String argument = argumentParameter == null ? "" : argumentParameter;
        boolean detailed = argumentHasDetailedMode(argument);
        SpanCatcher.debug = argumentHasDebugMode(argument);
        Optional<String> maybeFilePath = outputFileOnArgument(argument);

        Optional<File> file = maybeFilePath
                .map(File::new)
                .filter(f -> f.exists() || f.isDirectory());

        if(maybeFilePath.isPresent() && file.isEmpty()){
            System.err.println("[JAVA_AGENT] Bad directory: '"+maybeFilePath.get()+"'");
            System.err.println("[JAVA_AGENT] Directory needs to exist!");
            System.err.println("[JAVA_AGENT] Will use temporary instead");
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
            System.out.println("[JAVA_AGENT] Flamegraph output to '"+snapshotDirectory.getAbsolutePath()+"'");
        }));


        System.out.println("[JAVA_AGENT] Modes: debug:"+SpanCatcher.debug+" detailed:"+detailed+" output to '"+snapshotDirectory.getAbsolutePath()+"'");
        Advice advice = detailed ? Advice.to(SpanCatcherDetailed.class) : Advice.to(SpanCatcher.class);
        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE);
        if(SpanCatcher.debug){
            agentBuilder = agentBuilder.with(new DebugListener());
        }
        agentBuilder
            .type(not(nameContains("com.github.beothorn.agent")))
            .transform(
                (
                    builder,
                    typeDescription,
                    classLoader,
                    module,
                    protectionDomain
                ) -> {
                    if(SpanCatcher.debug){
                        System.out.println("[JAVA_AGENT] Transform '"+typeDescription.getCanonicalName()+"'");
                    }
                    return builder.visit(advice.on(ElementMatchers.isMethod()));
                }
            )
            .installOn(instrumentation);
    }

    public static boolean argumentHasDetailedMode(String argument){
        return argument.contains("mode:detailed");
    }

    public static boolean argumentHasDebugMode(String argument){
        return argument.contains("mode:debug");
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
                throw new RuntimeException("Jar is corrupted, missing file '"+ fileToBeExtracted +"'");
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
            System.out.println("[JAVA_AGENT] onDiscovery(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }

        @Override
        public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
            System.out.println("[JAVA_AGENT] onTransformation(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "DynamicType dynamicType='"+dynamicType+"')");
        }

        @Override
        public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
            System.out.println("[JAVA_AGENT] onIgnored(TypeDescription typeDescription='"+typeDescription+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }

        @Override
        public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            System.out.println("[JAVA_AGENT] onError(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"', " +
                    "Throwable throwable='"+throwable+"')");
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            System.out.println("[JAVA_AGENT] onComplete(String typeName='"+typeName+"', " +
                    "ClassLoader classLoader='"+classLoader+"', " +
                    "JavaModule module='"+module+"', " +
                    "boolean loaded='"+loaded+"')");
        }
    }
}