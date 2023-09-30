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
    public static long enter(@Advice.Origin Method method) {
        try {
            String methodName = method.getName();
            final String threadName = Thread.currentThread().getName();
            return onEnter(threadName, methodName);
        } catch (Exception e){
            log(DEBUG, e.getMessage());
            return -1;
        }
    }

    public static long onEnter(final String threadName, final String methodName){
        log(DEBUG, "Enter @"+threadName+": "+methodName);

        final Span stack = stackPerThread.get(threadName);
        if(stack == null){
            stackPerThread.put(threadName, span(methodName));
        } else {
            Span enter = stack.enter(methodName);
            stackPerThread.put(threadName, enter);
        }
        return System.currentTimeMillis();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long start) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            final String threadName = Thread.currentThread().getName();
            onLeave(threadName, start, currentTimeMillis);
        } catch (Exception e){
            log(DEBUG, e.getMessage());
        }
    }

    public static void onLeave(final String threadName, final long start, final long currentTimeMillis) {
        final Span stack = stackPerThread.get(threadName);
        long executionTime = (start == -1)? 0 : currentTimeMillis - start;
        stack.value(executionTime);
        Span leave = stack.leave();
        if(leave == null){
            return;
        }
        stackPerThread.put(threadName, leave);
        log(DEBUG, "Leave @"+threadName+": "+stack.description());
    }

    public static String getOldCallStack() {

        Map<String, Span> oldStackPerThread = new ConcurrentHashMap<>();

        SpanCatcher.stackPerThread.forEach((key, value) -> {
            if(value != null) value.removePastSpans().ifPresent(p -> {
                oldStackPerThread.put(key, p);
            });
        });

        return "["+oldStackPerThread
                .entrySet()
                .stream()
                .map(eS -> "{\"thread\":\""+eS.getKey()+"\",\"span\":"+eS.getValue().getRoot().toJson()+"}")
                .collect(Collectors.joining(","))+"]";
    }

    public static String getFinalCallStack() {
        return "["+SpanCatcher.stackPerThread
                .entrySet()
                .stream()
                .map(eS -> "{\"thread\":\""+eS.getKey()+"\",\"span\":"+eS.getValue().getRoot().toJson()+"}")
                .collect(Collectors.joining(","))+"]";
    }
}