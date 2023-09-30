package com.github.beothorn.agent;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;
import static com.github.beothorn.agent.SpanCatcher.onEnter;
import static com.github.beothorn.agent.SpanCatcher.onLeave;

public class SpanCatcherDetailed {

    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin Method method, @Advice.AllArguments Object[] allArguments) {
        try {
            String methodName = method.getName();
            String ownerClass = method.getDeclaringClass().getName();
            StringBuilder prettyCall = new StringBuilder(ownerClass + "." + methodName + "(");
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                String argToString;
                if (allArguments[i].getClass().isArray()) {
                    argToString = Arrays.toString((Object[]) allArguments[i]);
                } else {
                    argToString = allArguments[i].toString();
                }
                prettyCall.append(parameter.getType().getName())
                        .append(" ")
                        .append(parameter.getName())
                        .append(" = ")
                        .append(argToString);
                if(i < parameters.length-1){
                    prettyCall.append(", ");
                }
            }
            prettyCall.append(")");
            final String threadName = Thread.currentThread().getName();
            long entryTime = System.currentTimeMillis();
            onEnter(threadName, prettyCall.toString(), entryTime);
            return entryTime;
        } catch (Exception e){
            log(DEBUG, e.getMessage());
            return 0;
        }
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long start) {
        try{
            long exitTime = System.currentTimeMillis();
            final String threadName = Thread.currentThread().getName();
            onLeave(threadName, exitTime);
        }catch (Exception e){
            log(DEBUG, e.getMessage());
        }
    }
}