package com.github.beothorn.agent;

import com.github.beothorn.agent.advice.AdviceConstructorCallRecorder;
import com.github.beothorn.agent.advice.AdviceConstructorCallRecorderWithCapture;
import com.github.beothorn.agent.advice.AdviceFunctionCallRecorder;
import com.github.beothorn.agent.advice.AdviceFunctionCallRecorderWithCapture;
import com.github.beothorn.agent.logging.Log;
import com.github.beothorn.agent.parser.ClassAndMethodMatcher;
import com.github.beothorn.agent.parser.CompilationException;
import com.github.beothorn.agent.parser.ElementMatcherFromExpression;
import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import com.github.beothorn.agent.transformer.ConstructorInterceptor;
import com.github.beothorn.agent.transformer.DebugListener;
import com.github.beothorn.agent.transformer.MethodAndConstructorInterception;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.beothorn.agent.CommandLine.Flag.*;
import static com.github.beothorn.agent.logging.Log.LogLevel.*;
import static com.github.beothorn.agent.parser.ElementMatcherFromExpression.forExpression;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class MethodInstrumentationAgent {

    private static File snapshotDirectory;

    private static final long SAVE_SNAPSHOT_INTERVAL_MILLIS = 1000L;

    private static final ReentrantLock fileWriteLock = new ReentrantLock();

    public static Log.LogLevel currentLevel = ERROR;

    public static void premain(
        String argumentParameter,
        Instrumentation instrumentation
    ) {
        String argument = argumentParameter == null ? "" : argumentParameter;

        currentLevel = CommandLine.argumentLogLevel(argument);
        Log.log(INFO, "Agent loaded");
        boolean shouldCaptureValues = !CommandLine.argumentHasNoCaptureValuesMode(argument);
        FunctionCallRecorder.setShouldCaptureStacktrace(CommandLine.argumentHasShouldCaptureStackTraces(argument));
        Optional<String> maybeFilePath = CommandLine.outputFileOnArgument(argument);

        Optional<File> file = maybeFilePath
                .map(File::new)
                .filter(f -> f.exists() || f.isDirectory());

        if(maybeFilePath.isPresent() && !file.isPresent()){
            Log.log(ERROR, "Bad directory: '"+maybeFilePath.get()+"'");
            Log.log(ERROR, "Directory needs to exist!");
            Log.log(ERROR, "Will use temporary instead");
        }

        File javaFlameDirectory;
        try {
            javaFlameDirectory = file.orElse(Files.createTempDirectory("javaflame").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        snapshotDirectory = new File(javaFlameDirectory.getAbsolutePath(), System.currentTimeMillis() + "_snap");
        Log.log(INFO, "Output at "+snapshotDirectory.getAbsolutePath());
        if(!snapshotDirectory.mkdir()){
            Log.log(ERROR, "Could not create dir "+snapshotDirectory.getAbsolutePath());
        }

        writeHtmlFiles();

        Optional<String> filter = CommandLine.argumentFilter(argument);
        Optional<String> maybeStartRecordingTriggerFunction = CommandLine.argumentStartRecordingTriggerFunction(argument);
        Optional<String> maybeStopRecordingTriggerFunction = CommandLine.argumentStopRecordingTriggerFunction(argument);

        String[] allFlags = allFlagsOnArgument(argument);
        String allFlagsAsString = Arrays.toString(allFlags);
        String outputDirectory = snapshotDirectory.getAbsolutePath();

        String filterString = filter.orElse("No filter parameter");
        String executionMetadata = "logLevel :" + currentLevel.name()
                + " flags:" + allFlagsAsString
                + " output to '" + outputDirectory + "'"
                + " filters:" + filterString
                + maybeStartRecordingTriggerFunction.map(s -> "Start recording trigger function" + s).orElse("")
                + maybeStopRecordingTriggerFunction.map(s -> "Stop recording trigger function" + s).orElse("");
        Log.log(DEBUG, executionMetadata);

        String executionMetadataFormatted = getExecutionMetadataAsHtml(
            allFlagsAsString,
            outputDirectory,
            filterString,
            maybeStartRecordingTriggerFunction,
            maybeStopRecordingTriggerFunction
        );

        maybeStartRecordingTriggerFunction.ifPresent(FunctionCallRecorder::setStartTrigger);
        maybeStopRecordingTriggerFunction.ifPresent(FunctionCallRecorder::setStopTrigger);

        ElementMatcher.Junction<NamedElement> exclusion = not(
            nameStartsWith("com.github.beothorn.agent").or(nameStartsWith("net.bytebuddy"))
        );

        Optional<String> interceptConstructorFilter = CommandLine.argumentInterceptConstructorFilter(argument);


        Optional<ElementMatcherFromExpression> elementMatcherFromFilterExpression = filter.map(f -> {
            try {
                return forExpression(f);
            } catch (CompilationException e) {
                throw new RuntimeException(e);
            }
        });


        Optional<ElementMatcherFromExpression> elementMatcherFromInterceptConstructorFilterExpression = interceptConstructorFilter.map(f -> {
            try {
                return forExpression(f);
            } catch (CompilationException e) {
                throw new RuntimeException(e);
            }
        });


        ElementMatcher.Junction<NamedElement> filterMatcher = elementMatcherFromFilterExpression
                .map(m -> exclusion.and(m.getClassMatcher()))
                .orElse(exclusion);

        ElementMatcher.Junction<NamedElement> constructorInterceptMatcher = elementMatcherFromInterceptConstructorFilterExpression
                .map(m -> exclusion.and(m.getClassMatcher()))
                .orElse(none());

        AgentBuilder agentBuilder = new AgentBuilder.Default();

        boolean coreClassesMode = CommandLine.argumentHasIncludeCoreClasses(argument);
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

        List<ClassAndMethodMatcher> classAndMethodMatchers = elementMatcherFromFilterExpression
                .map(ElementMatcherFromExpression::getClassAndMethodMatchers)
                .orElse(new ArrayList<>());

        Optional<String> constructorInterceptorToCall = CommandLine.argumentConstructorInterceptor(argument);

        DebugListener debugListener = new DebugListener();
        AgentBuilder builder = agentBuilder
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
            .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
            .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
            .with(debugListener);

        if (constructorInterceptorToCall.isPresent()) {
            builder = builder.type(constructorInterceptMatcher)
                    .transform(new ConstructorInterceptor(constructorInterceptorToCall.get()));
        }

        MethodAndConstructorInterception methodAndConstructorInterception = new MethodAndConstructorInterception(
            adviceForFunction,
            adviceForConstructor,
            classAndMethodMatchers
        );

        builder
            .type(filterMatcher)
            .transform(methodAndConstructorInterception)
            .installOn(instrumentation);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            writeSnapshotToFile(executionMetadataFormatted, debugListener);
        }));

        Thread snapshotThread = new Thread(() -> {
            while (true) {
                writeSnapshotToFile(executionMetadataFormatted, debugListener);
                try {
                    Thread.sleep(SAVE_SNAPSHOT_INTERVAL_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        if(!CommandLine.argumentHasNoSnapshotsMode(argument)){
            snapshotThread.setDaemon(true);
            snapshotThread.start();
        }
    }

    public static String getExecutionMetadataAsHtml(
        String allFlagsAsString,
        String outputDirectory,
        String filter,
        Optional<String> maybeStartRecordingTriggerFunction,
        Optional<String> maybeStopRecordingTriggerFunction
    ) {
        return "<p>Flags: " + allFlagsAsString + "</p>"
                + "<p>Output: '" + outputDirectory + "'</p>"
                + "<p>Filters: " + filter + "</p>"
                + maybeStartRecordingTriggerFunction.map(s -> "<p>Start recording trigger function: '" + s + "'</p>").orElse("")
                + maybeStopRecordingTriggerFunction.map(s -> "<p>Stop recording trigger function: '" + s + "'</p>").orElse("");
    }

    private static void writeSnapshotToFile(String executionMetadataFormatted, DebugListener debugListener) {
        fileWriteLock.lock();
        String snapshotDirectoryAbsolutePath = snapshotDirectory.getAbsolutePath();
        try {
            FunctionCallRecorder.getOldCallStack().ifPresent(oldCallStack -> {
                try {
                    File dataFile = new File(snapshotDirectoryAbsolutePath, "data.js");
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
                    Log.log(INFO, "Snapshot '" + dataFile.getAbsolutePath() + "'");
                    Log.log(INFO, "Graph at '" + dataFile.getParentFile().getAbsolutePath()
                            + FileSystems.getDefault().getSeparator() + "index.html'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } finally {
            fileWriteLock.unlock();
        }

        if (currentLevel.shouldPrint(DEBUG)) {
            debugListener.writeDebugFiles(snapshotDirectoryAbsolutePath);
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
}