package com.github.beothorn.agent;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;

public class SpanCatcher {
    public static final Map<String, ArrayDeque<Span>> stackPerThread = new ConcurrentHashMap<>();

    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin Method method) {
        try {
            String methodName = method.getName();
            final String threadName = Thread.currentThread().getName();
            return onEnter(threadName, methodName);
        } catch (Exception e){
            log(DEBUG, e.getMessage());
            return 0;
        }
    }

    public static long onEnter(final String threadName, final String methodName){
        log(DEBUG, "Enter @"+threadName+": "+methodName);
        final ArrayDeque<Span> stack = stackPerThread.getOrDefault(threadName, new ArrayDeque<>());
        stack.push(Span.span(methodName));
        stackPerThread.put(threadName, stack);
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
        final ArrayDeque<Span> stack = stackPerThread.get(threadName);
        Span current = stack.pop();
        long executionTime = currentTimeMillis - start;
        current.value(executionTime);
        Span parent = stack.peek();
        log(DEBUG, "Leave @"+threadName+": "+current.description());
        if(parent == null){
            stack.push(current);
        }else{
            parent.add(current);
        }
    }

    public static String getFinalCallStack() {
        return "["+SpanCatcher.stackPerThread
                .entrySet()
                .stream()
                .map(eS -> "{\"thread\":\""+eS.getKey()+"\",\"span\":"+eS.getValue().getLast().toJson()+"}")
                .collect(Collectors.joining(","))+"]";
    }
}