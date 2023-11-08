package com.github.beothorn.agent;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;
import static com.github.beothorn.agent.Span.span;

/**
 * This class is responsible for recording the call stack of a function call.
 * Usually, this should be written following OO principles of encapsulation.
 * But this class bytecode is injected on the actual code, and having stuff private
 * seems to cause some issues. You will see some questionable visibility choices here.
 */
public class FunctionCallRecorder {
    public static final Map<String, Span> stackPerThread = new ConcurrentHashMap<>();
    public static boolean shouldPrintQualified = false;
    public static boolean isRecording = true;
    public static String startTrigger;
    public static String stopTrigger;

    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin Method method) {
        try {
            String methodName = method.getName();
            String ownerClass = getClassNameFor(method);

            String finalMethodSignature = ownerClass + "." + methodName;

            final String threadName = Thread.currentThread().getName();
            long entryTime = System.currentTimeMillis();
            onEnter(threadName, finalMethodSignature, methodName, entryTime);
        } catch (Exception e){
            // Should never get here, but if it does, execution needs to go on
            log(DEBUG, e.getMessage());
        }
    }

    public static void setStartTrigger(String startTriggerFunctionNam) {
        startTrigger = startTriggerFunctionNam;
        isRecording = false;
    }

    public static void setStopTrigger(String stopTriggerFunctionNam) {
        stopTrigger = stopTriggerFunctionNam;
    }

    public static String getClassNameFor(Method method) {
        String ownerClass;
        Class<?> declaringClass = method.getDeclaringClass();
        if ( FunctionCallRecorder.shouldPrintQualified ){
            ownerClass = declaringClass.getName();
        } else {
            ownerClass = declaringClass.getSimpleName();
        }
        return ownerClass;
    }

    public static void onEnter(
        final String threadName,
        final String name,
        final String method,
        final long entryTime
    ){
        onEnter(
            threadName,
            name,
            method,
            entryTime,
            null
        );
    }

    public static void onEnter(
            final String threadName,
            final String name,
            final String method,
            final long entryTime,
            String[][] arguments
    ){
        if(isRecording && method.equals(stopTrigger)){
            isRecording = false;
            return;
        }

        if(!isRecording && method.equals(startTrigger)){
            isRecording = true;
        }

        if(!isRecording){
            log(DEBUG, "Skip @"+threadName+": "+name);
            return;
        }

        log(DEBUG, "Enter @"+threadName+": "+name);

        Span stack = getCurrentRunning(threadName);
        if(stack == null){
            stackPerThread.put(
                threadName,
                span(
                    threadName + "Root",
                    threadName + "Root",
                    entryTime,
                    arguments
                )
            );
        }
        stack = getCurrentRunning(threadName);

        Span newCurrentRunning = stack.enter(
            name,
            method,
            entryTime,
            arguments
        );
        stackPerThread.put(threadName, newCurrentRunning);
    }

    private static Span getCurrentRunning(String threadName) {
        return stackPerThread.get(threadName);
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            final String threadName = Thread.currentThread().getName();
            onLeave(threadName, currentTimeMillis);
        } catch (Exception e){
            log(DEBUG, e.getMessage());
        }
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
        final Span stack = getCurrentRunning(threadName);
        Span leave = stack.leave(exitTime, returnValue);
        if(leave == null){ // Valid state, this could mean we started recording in a child function call
            log(DEBUG, "Leaving root loop on "+threadName+" last stack: "+stack.description());
            stack.exitTime = exitTime;
            return;
        }
        stackPerThread.put(threadName, leave);
        log(DEBUG, "Leave @"+threadName+": "+stack.description());
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