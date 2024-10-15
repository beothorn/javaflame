package com.github.beothorn.agent.recorder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.github.beothorn.agent.logging.Log.LogLevel.*;
import static com.github.beothorn.agent.logging.Log.log;
import static com.github.beothorn.agent.recorder.Span.span;

/**
 * This class is responsible for recording the call stack of a function call.
 * Usually, this should be written following OO principles of encapsulation.
 * But this class bytecode is injected on the actual code, and having stuff private
 * seems to cause some issues. You will see some questionable visibility choices here.
 */
public class FunctionCallRecorder {
    public static final Map<String, Span> stackPerThread = new ConcurrentHashMap<>();
    public static boolean shouldPrintQualified = false;
    public static boolean shouldCaptureStacktrace = false;
    public static boolean isRecording = true;
    public static String startTrigger;
    public static String stopTrigger;

    public static void enterFunction(Method method) {
        String methodName = method.getName();
        String ownerClass = getClassNameFor(method);

        String finalMethodSignature = ownerClass + "." + methodName;

        final String threadName = Thread.currentThread().getName();
        long entryTime = System.currentTimeMillis();
        String ownerClassFullName = method.getDeclaringClass().getName();
        onEnter(
            threadName,
            finalMethodSignature,
            ownerClassFullName,
            methodName,
            entryTime
        );
    }
    public static void enterConstructor(Constructor<?> constructor) {
        String methodName = "new";
        String ownerClass = getClassNameFor(constructor);

        String finalMethodSignature = ownerClass + "." + methodName;

        final String threadName = Thread.currentThread().getName();
        long entryTime = System.currentTimeMillis();
        String ownerClassFullName = constructor.getDeclaringClass().getName();
        onEnter(
                threadName,
                finalMethodSignature,
                ownerClassFullName,
                methodName,
                entryTime
        );
    }

    public static void exit() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            final String threadName = Thread.currentThread().getName();
            onLeave(threadName, currentTimeMillis);
        } catch (Exception e){
            log(ERROR, "On exit function on function call recorder" + e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }

    public static void setStartTrigger(String startTriggerFunctionNam) {
        startTrigger = startTriggerFunctionNam;
        isRecording = false;
    }

    public static void setShouldCaptureStacktrace(boolean captureStacktrace) {
        shouldCaptureStacktrace = captureStacktrace;
    }

    public static void setStopTrigger(String stopTriggerFunctionNam) {
        stopTrigger = stopTriggerFunctionNam;
    }

    public static String getClassNameFor(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return getClassNameFor(declaringClass);
    }

    private static String getClassNameFor(final Class<?> declaringClass) {
        String ownerClass;
        if ( FunctionCallRecorder.shouldPrintQualified ){
            ownerClass = declaringClass.getName();
        } else {
            ownerClass = declaringClass.getSimpleName();
        }
        return ownerClass;
    }

    public static String getClassNameFor(Constructor<?> constructor) {
        Class<?> declaringClass = constructor.getDeclaringClass();
        return getClassNameFor(declaringClass);
    }

    public static void onEnter(
        final String threadName,
        final String name,
        final String className,
        final String method,
        final long entryTime
    ){
        onEnter(
            threadName,
            name,
            className,
            method,
            entryTime,
            null
        );
    }

    public static void onEnter(
            final String threadName,
            final String name,
            final String className,
            final String method,
            final long entryTime,
            String[][] arguments
    ){
        String fullyQualified = className+"#"+method;

        if(!isRecording && fullyQualified.equals(startTrigger)){
            isRecording = true;
        }

        if(!isRecording){
            log(TRACE, "Skip @"+threadName+": "+name);
            return;
        }

        String stacktrace = null;
        if (shouldCaptureStacktrace) {
            stacktrace = Arrays.toString(Thread.currentThread().getStackTrace());
        }

        log(TRACE, "Enter @"+threadName+": "+name);

        Span stack = getCurrentRunning(threadName);
        if(stack == null){
            stackPerThread.put(
                threadName,
                span(
                    threadName + "Root",
                    threadName + "Root",
                    threadName + "Root",
                    entryTime,
                    arguments,
                    stacktrace
                )
            );
        }
        stack = getCurrentRunning(threadName);

        Span newCurrentRunning = stack.enter(
            name,
            className,
            method,
            entryTime,
            arguments,
            stacktrace
        );
        stackPerThread.put(threadName, newCurrentRunning);


        if(isRecording && fullyQualified.equals(stopTrigger)){
            isRecording = false;
        }
    }

    private static Span getCurrentRunning(String threadName) {
        return stackPerThread.get(threadName);
    }

    public static void onLeave(
            final String threadName,
            final long exitTime
    ) {
        onLeave(threadName, exitTime, null);
    }

    public static void onLeave(
            final String threadName,
            final long exitTime,
            final String[] returnValue
    ) {
        // Get the span at the top of thr stack for the thread
        final Span stack = getCurrentRunning(threadName);
        if(stack == null){ // Valid state, this could mean we started recording in a child function call
            log(TRACE, "Leaving root loop on "+threadName+" no stack");
            return;
        }
        // Leave span at the top of the stack and return parent
        Span leave = stack.leave(exitTime, returnValue);
        if(leave == null){ // Parent null is a valid state, this could mean we started recording in a child function call
            log(TRACE, "Leaving root loop on "+threadName+" last stack: "+stack.description());
            stack.exitTime = exitTime;
            return;
        }
        // Re-add the parent span
        stackPerThread.put(threadName, leave);
        log(TRACE, "Leave @"+threadName+": "+stack.description());
    }

    public static Optional<String> getOldCallStack() {
        if(FunctionCallRecorder.stackPerThread.isEmpty()) return Optional.empty();

        Map<String, Span> oldStackPerThread = new ConcurrentHashMap<>();

        FunctionCallRecorder.stackPerThread.forEach((key, value) -> {
            if(value != null) value.getRoot().removeFinishedFunction()
                    .ifPresent(p -> oldStackPerThread.put(key, p));
        });

        if(oldStackPerThread.isEmpty()) return Optional.empty();

        return Optional.of(getSnapshot(oldStackPerThread));
    }

    private static String getSnapshot(Map<String, Span> stackPerThreadToPrint) {
        return "[\n" + stackPerThreadToPrint
            .entrySet()
            .stream()
            .map(eS -> "{"
                    + "\"thread\":\"" + eS.getKey() + "\","
                    + "\"snapshotTime\":" + System.currentTimeMillis() + ","
                    + "\"span\":" + eS.getValue().getRoot().toJson()
                + "}")
            .collect(Collectors.joining(",")) + "\n]";
    }

    public static Optional<String> getFinalCallStack() {
        if(FunctionCallRecorder.stackPerThread.isEmpty()) return Optional.empty();

        return Optional.of(getSnapshot(FunctionCallRecorder.stackPerThread));
    }
}