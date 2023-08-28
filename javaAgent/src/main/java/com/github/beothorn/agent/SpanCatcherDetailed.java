package com.github.beothorn.agent;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class SpanCatcherDetailed {

    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin Method method, @Advice.AllArguments Object[] allArguments) {
        String methodName = method.getName();
        String ownerClass = method.getDeclaringClass().getName();
        StringBuilder prettyCall = new StringBuilder(ownerClass+"."+methodName + "(");
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            prettyCall.append(
                    parameter.getType().getName())
                    .append(" ")
                    .append(parameter.getName())
                    .append(" = ")
                    .append(allArguments[i].toString());
        }
        prettyCall.append(")");
        final String threadName = Thread.currentThread().getName();
        return SpanCatcher.onEnter(threadName, prettyCall.toString());
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long start) {
        long currentTimeMillis = System.currentTimeMillis();
        final String threadName = Thread.currentThread().getName();
        SpanCatcher.onLeave(threadName, start, currentTimeMillis);
    }
}