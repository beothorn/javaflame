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
        Path javaFlameDirectory;
        try {
            javaFlameDirectory = Files.createTempDirectory("javaflame");
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
                builder.visit(Advice.to(SpanCatcher.class).on(ElementMatchers.isMethod()))
            )
            .installOn(instrumentation);
    }

    private static void extractFromResources(final File snapdir, final String fileToBeExtracted) throws IOException {
        File indexHtmlOut = new File(snapdir.getAbsolutePath(), fileToBeExtracted);
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
