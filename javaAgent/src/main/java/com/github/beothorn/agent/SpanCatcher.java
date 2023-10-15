package com.github.beothorn.agent;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;
import static com.github.beothorn.agent.Span.span;

public class SpanCatcher {
    public static final Map<String, Span> stackPerThread = new ConcurrentHashMap<>();

    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin Method method) {
        try {
            String methodName = method.getName();
            final String threadName = Thread.currentThread().getName();
            long entryTime = System.currentTimeMillis();
            onEnter(threadName, methodName, entryTime);
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

        final Span stack = stackPerThread.get(threadName);
        if(stack == null){
            stackPerThread.put(threadName, span(methodName, entryTime));
        } else {
            Span enter = stack.enter(methodName, entryTime);
            stackPerThread.put(threadName, enter);
        }
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

    public static void onLeave(final String threadName, final long exitTime) {
        final Span stack = stackPerThread.get(threadName);
        Span leave = stack.leave(exitTime);
        if(leave == null){
            return;
        }
        stackPerThread.put(threadName, leave);
        log(DEBUG, "Leave @"+threadName+": "+stack.description());
    }

    public static String getOldCallStack() {
        Map<String, Span> oldStackPerThread = new ConcurrentHashMap<>();

        SpanCatcher.stackPerThread.forEach((key, value) -> {
            if(value != null) value.removeFinishedFunction().ifPresent(p -> {
                oldStackPerThread.put(key, p);
            });
        });

        return getSnapshot(oldStackPerThread);
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

    public static String getFinalCallStack() {
        return getSnapshot(SpanCatcher.stackPerThread);
    }
}