package com.github.beothorn.agent;

import com.github.beothorn.agent.advice.*;
import com.github.beothorn.agent.logging.Log;
import com.github.beothorn.agent.parser.ClassAndMethodMatcher;
import com.github.beothorn.agent.parser.CompilationException;
import com.github.beothorn.agent.parser.ElementMatcherFromExpression;
import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import com.github.beothorn.agent.transformer.CallRecorder;
import com.github.beothorn.agent.transformer.ConstructorInterceptor;
import com.github.beothorn.agent.transformer.DebugListener;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatcher.Junction;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static com.github.beothorn.agent.logging.Log.LogLevel.*;
import static com.github.beothorn.agent.logging.Log.log;
import static com.github.beothorn.agent.parser.ElementMatcherFromExpression.forExpression;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class MethodInstrumentationAgent {

    public static final String AGENT_PACKAGE = "com.github.beothorn.agent";
    public static final String BYTEBUDDY_PACKAGE = "net.bytebuddy";
    private static File snapshotDirectory;

    private static final long SAVE_SNAPSHOT_INTERVAL_MILLIS = 1000L;

    private static final ReentrantLock fileWriteLock = new ReentrantLock();

    public static Log.LogLevel currentLevel = ERROR;

    private static class MatchAndCall {

        public final ElementMatcherFromExpression matcher;
        public final String className;
        public final String methodName;

        private MatchAndCall(
            final ElementMatcherFromExpression matcher,
            final String className,
            final String methodName
        ){
            this.matcher = matcher;
            this.className = className;
            this.methodName = methodName;
        }
    }

    public static void premain(
        String argumentParameter,
        Instrumentation instrumentation
    ) {
        String argument = argumentParameter == null ? "" : argumentParameter;
        CommandLine.validateArguments(argument);

        currentLevel = CommandLine.argumentLogLevel(argument);
        log(INFO, "Agent loaded");
        FunctionCallRecorder.setShouldCaptureStacktrace(CommandLine.argumentHasShouldCaptureStackTraces(argument));

        File javaFlameDirectory = getOrCreateOutputDirectory(argument);

        snapshotDirectory = new File(javaFlameDirectory.getAbsolutePath(), System.currentTimeMillis() + "_snap");
        log(INFO, "Output at "+snapshotDirectory.getAbsolutePath());
        if(!snapshotDirectory.mkdir()){
            log(ERROR, "Could not create dir "+snapshotDirectory.getAbsolutePath());
        }

        writeHtmlFiles();

        Optional<String> maybeStartRecordingTriggerFunction = CommandLine.argumentStartRecordingTriggerFunction(argument);
        Optional<String> maybeStopRecordingTriggerFunction = CommandLine.argumentStopRecordingTriggerFunction(argument);

        String[] allFlags = CommandLine.allFlagsOnArgument(argument);
        String allFlagsAsString = Arrays.toString(allFlags);
        String outputDirectory = snapshotDirectory.getAbsolutePath();

        String filterString = CommandLine.argumentFilter(argument).orElse("No filter parameter");
        String executionMetadata = "logLevel :" + currentLevel.name()
                + " arguments:" + argument
                + " flags:" + allFlagsAsString
                + " output to '" + outputDirectory + "'"
                + " filters:" + filterString
                + maybeStartRecordingTriggerFunction.map(s -> "Start recording trigger function" + s).orElse("")
                + maybeStopRecordingTriggerFunction.map(s -> "Stop recording trigger function" + s).orElse("");
        log(DEBUG, executionMetadata);

        String executionMetadataFormatted = getExecutionMetadataAsHtml(
            argument,
            allFlagsAsString,
            outputDirectory,
            filterString,
            maybeStartRecordingTriggerFunction,
            maybeStopRecordingTriggerFunction
        );

        maybeStartRecordingTriggerFunction.ifPresent(FunctionCallRecorder::setStartTrigger);
        maybeStopRecordingTriggerFunction.ifPresent(FunctionCallRecorder::setStopTrigger);

        // Agent builder creation starts here
        AgentBuilder agentBuilder = new AgentBuilder.Default();
        boolean coreClassesMode = CommandLine.argumentHasIncludeCoreClasses(argument);
        if(coreClassesMode){
            agentBuilder = agentBuilder.ignore(none());
        }

        final AgentBuilder initialBuilder = createDefaultAgentBuilder(agentBuilder);

        final AgentBuilder builderMaybeWithConstructorInterceptor = maybeAddConstructorInterceptor(
            argument,
            initialBuilder
        );

        final AgentBuilder builderMaybeWithMethodInterceptor = maybeAddMethodInterceptor(
            argument,
            builderMaybeWithConstructorInterceptor
        );

        final AgentBuilder finalAgentBuilder = maybeAddFilter(
            argument,
            builderMaybeWithMethodInterceptor
        );

        // Install agent with all options from arguments
        finalAgentBuilder.installOn(instrumentation);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            writeSnapshotToFile(executionMetadataFormatted, new DebugListener());
        }));

        Thread snapshotThread = new Thread(() -> {
            while (true) {
                writeSnapshotToFile(executionMetadataFormatted, new DebugListener());
                try {
                    //noinspection BusyWait
                    Thread.sleep(SAVE_SNAPSHOT_INTERVAL_MILLIS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if(!CommandLine.argumentHasNoSnapshotsMode(argument)){
            snapshotThread.setDaemon(true);
            snapshotThread.start();
        }
    }

    private static AgentBuilder extendBuilder(
            final AgentBuilder builder,
            final Junction<NamedElement> matchType,
            final Transformer transformer
    ) {
        return builder
                .type(excludeAgentClassesToAvoidRecursiveLoop(matchType))
                .transform(transformer);
    }

    private static AgentBuilder maybeAddFilter(
        final String argument,
        final AgentBuilder builder
    ) {
        Optional<String> maybeFilter = CommandLine.argumentFilter(argument);
        boolean shouldCaptureValues = !CommandLine.argumentHasNoCaptureValuesMode(argument);
        return maybeFilter.map(f -> {
            ElementMatcherFromExpression elementMatcher;
            try {
                elementMatcher = forExpression(f);
                log(DEBUG, "elementMatcher: "+elementMatcher);
            } catch (CompilationException e) {
                throw new RuntimeException(e);
            }
            List<ClassAndMethodMatcher> classAndMethodMatchers = elementMatcher.getClassAndMethodMatchers();
            System.out.println(Arrays.toString(classAndMethodMatchers.toArray()));
            CallRecorder transformer = createCallRecorderForJavaFlame(
                shouldCaptureValues,
                classAndMethodMatchers
            );
            Junction<NamedElement> matchType = elementMatcher.getClassMatcher();
            return extendBuilder(builder, matchType, transformer);
        }).orElse(extendBuilder(builder, any(), createCallRecorderForJavaFlame(shouldCaptureValues))); // No filter captures all
    }

    private static AgentBuilder maybeAddConstructorInterceptor(
        final String argument,
        final AgentBuilder builder
    ) {
        Optional<String> interceptConstructorFilter = CommandLine.argumentInterceptConstructor(argument);
        Optional<MatchAndCall> forConstructorInterception = interceptConstructorFilter
                .map(MethodInstrumentationAgent::matchAndCallForFilter);
        return forConstructorInterception.map(fci -> {
            AdviceInterceptConstructor.classFullName = fci.className;
            AdviceInterceptConstructor.method = fci.methodName;
            Junction<NamedElement> matchType = fci.matcher.getClassMatcher();
            ConstructorInterceptor transformer = new ConstructorInterceptor();
            return extendBuilder(builder, matchType, transformer);
        }).orElse(builder);
    }

    private static AgentBuilder maybeAddMethodInterceptor(
        final String argument,
        final AgentBuilder builder
    ) {
        Optional<String> interceptConstructorFilter = CommandLine.argumentInterceptMethodEntry(argument);
        Optional<MatchAndCall> forConstructorInterception = interceptConstructorFilter
                .map(MethodInstrumentationAgent::matchAndCallForFilter);
        return forConstructorInterception.map(fci -> {
            AdviceInterceptMethod.classFullName = fci.className;
            AdviceInterceptMethod.method = fci.methodName;
            AdviceInterceptConstructorMethod.classFullName = fci.className;
            AdviceInterceptConstructorMethod.method = fci.methodName;
            Junction<NamedElement> matchType = fci.matcher.getClassMatcher();
            CallRecorder transformer = new CallRecorder(
                Advice.to(AdviceInterceptMethod.class),
                Advice.to(AdviceInterceptConstructorMethod.class),
                Advice.to(AdviceInterceptStaticMethod.class),
                fci.matcher.getClassAndMethodMatchers()
            );
            return extendBuilder(builder, matchType, transformer);
        }).orElse(builder);
    }

    private static AgentBuilder createDefaultAgentBuilder(final AgentBuilder agentBuilder) {
        return agentBuilder
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(new DebugListener());
    }

    private static Junction<NamedElement> excludeAgentClassesToAvoidRecursiveLoop(ElementMatcher<NamedElement> m) {
        return not(
            nameStartsWith(AGENT_PACKAGE).or(nameStartsWith(BYTEBUDDY_PACKAGE))
        ).and(m);
    }

    private static MatchAndCall matchAndCallForFilter(
        final String filter
    ) {
        String[] expressionAndMethodCall = filter.split(">");
        if (expressionAndMethodCall.length != 2) {
            throw new RuntimeException( "intercept expression expected to contain the character '>'");
        }
        String interceptConstructorExpression = expressionAndMethodCall[0];
        String interceptConstructorMethodCall = expressionAndMethodCall[1];
        String[] classAndMethodNameToCallOnConstructor = interceptConstructorMethodCall.split("#");
        if (classAndMethodNameToCallOnConstructor.length != 2) {
            throw new RuntimeException(
                    "intercept expression expected to contain a full method reference with the character '#'"
            );
        }
        ElementMatcherFromExpression elementMatcherFromExpression;
        try {
            elementMatcherFromExpression = forExpression(interceptConstructorExpression);
            log(DEBUG, "elementMatcherFromExpression: "+elementMatcherFromExpression);
        } catch (CompilationException e) {
            throw new RuntimeException(e);
        }

        String interceptConstructorClassToCall = classAndMethodNameToCallOnConstructor[0];
        String interceptConstructorMethodToCall = classAndMethodNameToCallOnConstructor[1];

        return new MatchAndCall(
            elementMatcherFromExpression,
            interceptConstructorClassToCall,
            interceptConstructorMethodToCall
        );
    }

    private static CallRecorder createCallRecorderForJavaFlame(
            final boolean shouldCaptureValues
    ) {
        return createCallRecorderForJavaFlame(shouldCaptureValues, new ArrayList<>());
    }

    private static CallRecorder createCallRecorderForJavaFlame(
        final boolean shouldCaptureValues,
        final List<ClassAndMethodMatcher> classAndMethodMatchers
    ) {
        Advice adviceForFunction;
        Advice adviceForConstructor;
        if (shouldCaptureValues){
            adviceForFunction = Advice.to(AdviceFunctionCallRecorderWithCapture.class);
            adviceForConstructor = Advice.to(AdviceConstructorCallRecorderWithCapture.class);
        } else {
            adviceForFunction = Advice.to(AdviceFunctionCallRecorder.class);
            adviceForConstructor = Advice.to(AdviceConstructorCallRecorder.class);
        }
        return new CallRecorder(
            adviceForFunction,
            adviceForConstructor,
            adviceForFunction,
            classAndMethodMatchers
        );
    }

    private static File getOrCreateOutputDirectory(final String argument) {
        Optional<File> file = getOutputDirectory(argument);

        File javaFlameDirectory;
        try {
            javaFlameDirectory = file.orElse(Files.createTempDirectory("javaflame").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return javaFlameDirectory;
    }

    private static Optional<File> getOutputDirectory(final String argument) {
        Optional<String> maybeFilePath = CommandLine.outputFileOnArgument(argument);
        Optional<File> file = maybeFilePath
                .map(File::new)
                .filter(f -> f.exists() || f.isDirectory());
        if(maybeFilePath.isPresent() && !file.isPresent()){
            log(ERROR, "Bad directory: '"+maybeFilePath.get()+"'");
            log(ERROR, "Directory needs to exist!");
            log(ERROR, "Will use temporary instead");
        }
        return file;
    }

    public static String getExecutionMetadataAsHtml(
        String arguments,
        String allFlagsAsString,
        String outputDirectory,
        String filter,
        Optional<String> maybeStartRecordingTriggerFunction,
        Optional<String> maybeStopRecordingTriggerFunction
    ) {
        return "<p>Arguments: " + arguments + "</p>"
                + "<p>Flags: " + allFlagsAsString + "</p>"
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
                    log(INFO, "Snapshot '" + dataFile.getAbsolutePath() + "'");
                    log(INFO, "Graph at '" + dataFile.getParentFile().getAbsolutePath()
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