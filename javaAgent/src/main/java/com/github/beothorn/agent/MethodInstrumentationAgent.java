package com.github.beothorn.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;

public class MethodInstrumentationAgent {

    private static File snapshotDirectory;

    public static void premain(
        String argument,
        Instrumentation instrumentation
    ) {
        Path javaFlameDirectory = null;
        boolean detailed = false;
        try {
            String[] arguments = argument.split(",");
            for (String keyValue : arguments) {
                String[] keyValeArg = keyValue.split(":");
                String key = keyValeArg[0];
                String value = keyValeArg[1];
                if (key.equalsIgnoreCase("mode")) {
                    detailed = value.equals("detailed");
                }
                if (key.equalsIgnoreCase("out")) {
                    File file = new File(value);
                    javaFlameDirectory = file.toPath();
                    if(!file.exists() || !file.isDirectory()){
                        System.err.println("Bad directory: '"+file.getAbsolutePath()+"'");
                        System.err.println("Directory needs to exist!");
                        System.err.println("Will use temporary instead");
                        javaFlameDirectory = null;
                    }
                }
            }
        }catch (ArrayIndexOutOfBoundsException exc){
            System.err.println("Bad arguments: '"+argument+"'");
            System.err.println("Expected: 'mode:detailed,out:/tmp/flameOut'");
            System.out.println("Using default values");
        }

        try {
            if(javaFlameDirectory == null){
                javaFlameDirectory = Files.createTempDirectory("javaflame");
            }
            snapshotDirectory = new File(javaFlameDirectory.toFile().getAbsolutePath(), System.currentTimeMillis() + "_snap");
            if(!snapshotDirectory.mkdir()){
                System.err.println("Could not create dir "+snapshotDirectory.getAbsolutePath());
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
            System.out.println("Flamegraph output to '"+snapshotDirectory.getAbsolutePath()+"'");
        }));

        Advice advice = detailed ? Advice.to(SpanCatcherDetailed.class) : Advice.to(SpanCatcher.class);
        new AgentBuilder.Default()
            .type(ElementMatchers.not(ElementMatchers.nameContains("com.github.beothorn.agent")))
            .transform(
                (
                    builder,
                    typeDescription,
                    classLoader,
                    module,
                    protectionDomain
                ) ->
                builder.visit(advice.on(ElementMatchers.isMethod()))
            )
            .installOn(instrumentation);
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
}