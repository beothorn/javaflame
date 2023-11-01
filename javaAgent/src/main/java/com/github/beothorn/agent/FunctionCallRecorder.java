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

public class FunctionCallRecorder {
    public static final Map<String, Span> stackPerThread = new ConcurrentHashMap<>();
    public static boolean shouldPrintQualified = false;

    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin Method method) {
        try {
            StringBuilder prettyCall = new StringBuilder();
            String methodName = method.getName();
            if ( FunctionCallRecorder.shouldPrintQualified ){
                String ownerClass = method.getDeclaringClass().getName();
                prettyCall.append(ownerClass)
                        .append(".")
                        .append(methodName);
            } else {
                prettyCall.append(methodName);
            }

            final String threadName = Thread.currentThread().getName();
            long entryTime = System.currentTimeMillis();
            onEnter(threadName, prettyCall.toString(), entryTime);
        } catch (Exception e){
            // Should never get here, but if it does, execution needs to go on
            log(DEBUG, e.getMessage());
        }
    }

    public static void onEnter(
        final String threadName,
        final String methodName,
        final long entryTime
    ){
        log(DEBUG, "Enter @"+threadName+": "+methodName);

        Span stack = getCurrentRunning(threadName);
        if(stack == null){
            stackPerThread.put(threadName, span(threadName + "Root", entryTime));
        }
        stack = getCurrentRunning(threadName);

        Span newCurrentRunning = stack.enter(methodName, entryTime);
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
            final String returnValue
    ) {
        final Span stack = getCurrentRunning(threadName);
        Span leave = stack.leave(exitTime, returnValue);
        if(leave == null){
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