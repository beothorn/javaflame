package com.github.beothorn.agent;

import com.github.beothorn.agent.advice.*;
import com.github.beothorn.agent.logging.Log;
import com.github.beothorn.agent.parser.ClassAndMethodMatcher;
import com.github.beothorn.agent.parser.CompilationException;
import com.github.beothorn.agent.parser.ElementMatcherFromExpression;
import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import com.github.beothorn.agent.transformer.CallRecorder;
import com.github.beothorn.agent.transformer.DebugListener;
import com.github.beothorn.agent.webserver.WebServer;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatcher.Junction;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.Manifest;

import static com.github.beothorn.agent.logging.Log.LogLevel.*;
import static com.github.beothorn.agent.logging.Log.log;
import static com.github.beothorn.agent.parser.ElementMatcherFromExpression.forExpression;
import static net.bytebuddy.matcher.ElementMatchers.*;

/***
 * This is the entrypoint for the agent.
 * Here all arguments will be parsed.
 * The advices will be wired in accordance to the filters.
 * The snapshot thread will be started.
 */
public class MethodInstrumentationAgent {

    public static final String VERSION = "v27.0.0";

    // These packages needs to be ignored. They belong to the agent.
    public static final String AGENT_PACKAGE = "com.github.beothorn.agent";
    public static final String BYTEBUDDY_PACKAGE = "net.bytebuddy";

    // When the agent runs from gradle inside intellij, for some reason premain is called twice
    // This boolean will avoid a second execution
    private static boolean alreadyCalled = false;

    // The interval when the snapshot is stored. This will be configurable in the future.
    // there is also a stack cleanup that happens when the snapshot happens, so this relates to memory consumption.
    private static final long SAVE_SNAPSHOT_INTERVAL_MILLIS = 1000L;

    // No concurrency problems when writing the snapshot.
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
        try {
            premainInternal(argumentParameter, instrumentation);
        } catch (Exception e){
            System.err.println("AGENT FAILED TO LOAD");
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public static void premainInternal(
        String argumentParameter,
        Instrumentation instrumentation
    ) {
        String argument = argumentParameter == null ? "" : argumentParameter;
        CommandLine.validateArguments(argument);
        currentLevel = CommandLine.argumentLogLevel(argument);

        // This may happen when running javaflame from intellij with gradle
        if(MethodInstrumentationAgent.alreadyCalled) {
            log(WARN, "Called premain twice! This may happen when running with gradle. Will ignore call");
            return;
        }
        // If premain is called again, it just returns
        MethodInstrumentationAgent.alreadyCalled = true;

        log(INFO, "Javaflame Agent " + VERSION + " loaded");

        if(CommandLine.argumentHasOff(argument)) {
            log(INFO, "OFF flag is on, will do nothing.");
            return;
        }

        // FunctionCallRecorder may run without capturing argument values if no_capturing_values is set
        FunctionCallRecorder.setShouldCaptureStacktrace(CommandLine.argumentHasShouldCaptureStackTraces(argument));

        Optional<String> maybeStartRecordingTriggerFunction = CommandLine.argumentStartRecordingTriggerFunction(argument);
        Optional<String> maybeStopRecordingTriggerFunction = CommandLine.argumentStopRecordingTriggerFunction(argument);

        String[] allFlags = CommandLine.allFlagsOnArgument(argument);
        String allFlagsAsString = Arrays.toString(allFlags);

        // The Main class from the last manifest found
        String app = null;
        // The path of the last manifest
        String path = null;

        try {
            Enumeration<URL> resources = MethodInstrumentationAgent.class
                    .getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                InputStream is = url.openStream();
                Manifest manifest = new Manifest(is);
                String mainClass = manifest.getMainAttributes().getValue("Main-Class");
                if(mainClass != null && !mainClass.equals(MethodInstrumentationAgent.class.getName())){
                    log(DEBUG, "Found a manifest with a main class: " + mainClass);
                    app = mainClass;
                    String manifestPath = url.getFile();
                    int lastExclamationMarkIndex = manifestPath.lastIndexOf("!");
                    path = manifestPath.substring(0, lastExclamationMarkIndex == -1 ? manifestPath.length() : lastExclamationMarkIndex );
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File javaFlameDirectory = getOutputDirectory(argument);
        File snapshotDirectory = new File( javaFlameDirectory.getAbsolutePath(), "javaflame");
        try {
            log(INFO, "Output at "+snapshotDirectory.getCanonicalPath());
        } catch (IOException e) {
            log(ERROR, e.getMessage());
        }
        if(!snapshotDirectory.exists() && !snapshotDirectory.mkdir()){
            log(ERROR, "Could not create dir "+snapshotDirectory.getAbsolutePath());
        }
        String outputDirectory = snapshotDirectory.getAbsolutePath();

        writeHtmlFiles(snapshotDirectory);

        String mainClassPackage = null;

        if(app != null){
            int lastDotIndex = app.lastIndexOf(".");
            mainClassPackage = app.substring(0, lastDotIndex == -1 ? app.length() : lastDotIndex );
        }

        Optional<String> maybeFilter = CommandLine.argumentFilter(argument);

        if(!maybeFilter.isPresent()){
            log(INFO, "Filter should not be empty.");
        }

        if(!maybeFilter.isPresent() && mainClassPackage != null){
            log(INFO, "Agent will use main class package as filter: " + mainClassPackage);
            maybeFilter = Optional.of(mainClassPackage);
        }

        String executionMetadata = "logLevel :" + currentLevel.name()
                + " arguments:" + argument
                + " flags:" + allFlagsAsString
                + " output to '" + outputDirectory + "'"
                + " filters:" + maybeFilter.orElse("No filter")
                + maybeStartRecordingTriggerFunction.map(s -> "Start recording trigger function" + s).orElse("")
                + maybeStopRecordingTriggerFunction.map(s -> "Stop recording trigger function" + s).orElse("");
        log(DEBUG, executionMetadata);

        String executionMetadataFormatted = getExecutionMetadataAsJson(
            app,
            path,
            argument,
            allFlagsAsString,
            outputDirectory,
            maybeFilter.orElse("No filter"),
            maybeStartRecordingTriggerFunction,
            maybeStopRecordingTriggerFunction
        );

        maybeStartRecordingTriggerFunction.ifPresent(FunctionCallRecorder::setStartTrigger);
        maybeStopRecordingTriggerFunction.ifPresent(FunctionCallRecorder::setStopTrigger);

        // Agent builder creation starts here
        AgentBuilder agentBuilder = new AgentBuilder.Default(new ByteBuddy().with(TypeValidation.DISABLED));
        boolean coreClassesMode = CommandLine.argumentHasIncludeCoreClasses(argument);
        if(coreClassesMode){
            agentBuilder = agentBuilder.ignore(none());
        }

        final AgentBuilder initialBuilder = createDefaultAgentBuilder(agentBuilder);

        final AgentBuilder builderMaybeWithMethodInterceptor = maybeAddMethodInterceptor(
            argument,
            initialBuilder
        );

        final AgentBuilder finalAgentBuilder = maybeAddFilter(
            maybeFilter,
            argument,
            builderMaybeWithMethodInterceptor
        );

        // Install agent with all options from arguments
        finalAgentBuilder.installOn(instrumentation);

        File dataFile = new File(snapshotDirectory, "data.js");
        if(dataFile.exists()) {
            dataFile.delete();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            writeSnapshotToFile(snapshotDirectory, new DebugListener());
        }));


        String content = "var executionMetadata = "+ executionMetadataFormatted +";\n";
        File metaFile = new File(snapshotDirectory, "meta.js");
        try (FileWriter fw = new FileWriter(metaFile)) {
            fw.write(content);
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread snapshotThread = new Thread(() -> {
            while (true) {
                writeSnapshotToFile(snapshotDirectory, new DebugListener());
                try {
                    //noinspection BusyWait
                    Thread.sleep(SAVE_SNAPSHOT_INTERVAL_MILLIS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        snapshotThread.setName("Javaflame Snapshot");
        snapshotThread.setDaemon(true);

        if(!CommandLine.argumentHasNoSnapshotsMode(argument)){
            snapshotThread.start();
        }

        Optional<Integer> maybePort = CommandLine.argumentServerPort(argument);
        maybePort.ifPresent(port -> {
            try {
                log(INFO, "Will start server at http://localhost:" + port +
                        "\nRecording is paused, open your browser to start recording.");
                FunctionCallRecorder.isRecording = false;
                WebServer.start(outputDirectory, port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
            final Optional<String> maybeFilter,
            final String argument,
            final AgentBuilder builder
    ) {
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
            CallRecorder transformer = createCallRecorderForJavaFlame(
                    shouldCaptureValues,
                    classAndMethodMatchers
            );
            Junction<NamedElement> matchType = elementMatcher.getClassMatcher();
            return extendBuilder(builder, matchType, transformer);
        }).orElse(extendBuilder(builder, any(), createCallRecorderForJavaFlame(shouldCaptureValues))); // No filter captures all
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

    /***
     * Gets a filter for an interceptor in the format FILTER>INTERCEPTOR_METHOD .
     * This will retun a class with the parsed filter an the interceptor class and method name.
     * @param filter
     * @return
     */
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

    private static File getOutputDirectory(final String argument) {
        Optional<String> maybeFilePath = CommandLine.outputFileOnArgument(argument);

        String defaultOutput = ".";
        String output = maybeFilePath.orElse(defaultOutput);

        File fileOutput = Paths.get(output).toFile();
        File workingDir = Paths.get(defaultOutput).toFile();

        if(!fileOutput.exists()){
            log(ERROR, "Directory does not exist: '"+fileOutput+"'");
            log(ERROR, "Will use: '"+workingDir.getAbsolutePath()+"'");
            return workingDir;
        }
        if(!fileOutput.isDirectory()){
            log(ERROR, "Not a directory: '"+fileOutput+"'");
            log(ERROR, "Will use: '"+workingDir.getAbsolutePath()+"'");
            return workingDir;
        }
        try {
            return fileOutput.getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getExecutionMetadataAsJson(
        String app,
        String path,
        String arguments,
        String allFlagsAsString,
        String outputDirectory,
        String filter,
        Optional<String> maybeStartRecordingTriggerFunction,
        Optional<String> maybeStopRecordingTriggerFunction
    ) {
        return "{" +
                "\"app\":\""+app+"\"," +
                "\"workingDir\":\""+System.getProperty("user.dir")+"\"," +
                "\"path\":\""+path+"\"," +
                "\"arguments\":\""+arguments+"\"," +
                "\"flags\":\""+allFlagsAsString+"\"," +
                "\"output\":\""+outputDirectory+"\"," +
                "\"filters\":\""+filter+"\"," +
                maybeStartRecordingTriggerFunction.map(s -> "\"startFun\":\"" + s + "\",").orElse("") +
                maybeStopRecordingTriggerFunction.map(s -> "\"stopFun\":\"" + s + "\",").orElse("") +
                "}";
    }

    private static void writeSnapshotToFile(
        File snapshotDirectory,
        DebugListener debugListener
    ) {
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
                            String content = "var data = [" + oldCallStack + ",\n];";
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

    private static void writeHtmlFiles(File snapshotDirectory) {
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
        if(indexHtmlOut.exists()) {
            indexHtmlOut.delete();
        }
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