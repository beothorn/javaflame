package com.github.beothorn.agent;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SpanCatcher {
    public static final Map<String, ArrayDeque<Span>> stackPerThread = new ConcurrentHashMap<>();

    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin Method method) {
        String methodName = method.getName();
        return onEnter(methodName);
    }

    public static long onEnter(final String methodName) {
        final String threadName = Thread.currentThread().getName();
        final ArrayDeque<Span> stack = stackPerThread.getOrDefault(threadName, new ArrayDeque<>());
        stack.push(Span.span(methodName));
        stackPerThread.put(threadName, stack);
        return System.currentTimeMillis();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long start) {
        long currentTimeMillis = System.currentTimeMillis();
        onLeave(start, currentTimeMillis);
    }

    public static void onLeave(final long start, final long currentTimeMillis) {
        final String threadName = Thread.currentThread().getName();
        final ArrayDeque<Span> stack = stackPerThread.get(threadName);
        Span current = stack.pop();
        long executionTime = currentTimeMillis - start;
        current.value(executionTime);
        Span parent = stack.peek();
        if(parent == null){
            stack.push(current);
        }else{
            parent.add(current);
        }
    }

    public static String getFinalCallStack() {
        System.out.println("size "+SpanCatcher.stackPerThread.size());
        return SpanCatcher.stackPerThread
                .entrySet()
                .stream()
                .map(eS -> "{\"thread\":\""+eS.getKey()+"\",\"span\":"+eS.getValue().getLast().toJson()+"}")
                .collect(Collectors.joining(","));
    }
}