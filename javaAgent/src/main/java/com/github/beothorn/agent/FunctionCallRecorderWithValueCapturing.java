package com.github.beothorn.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.beothorn.agent.FunctionCallRecorder.*;
import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;

public class FunctionCallRecorderWithValueCapturing {

    public static final Map<String, Boolean> shouldDetailThread = new ConcurrentHashMap<>();

    @Advice.OnMethodEnter
    public static long enter(
        @Advice.Origin Method method,
        @Advice.AllArguments Object[] allArguments
    ) {
        try {
            StringBuilder prettyCall = new StringBuilder();
            String methodName = method.getName();
            String ownerClass = getClassNameFor(method);
            prettyCall.append(ownerClass)
                    .append(".")
                    .append(methodName)
                    .append("(");
            Parameter[] parameters = method.getParameters();
            String[][] arguments = new String[parameters.length][2];
            final String threadName = Thread.currentThread().getName();

            // We avoid extracting detail when executing a toString for a parameter
            // or else we risk creating a stack overflow
            Boolean createDetails = shouldDetailThread.getOrDefault(threadName, true);
            if (createDetails) {
                shouldDetailThread.put(threadName, false);
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    String argToString;
                    argToString = getValueAsString(allArguments[i]);
                    String paramType = getTypeAsString(allArguments, i, parameter);
                    prettyCall.append(paramType)
                            .append(" = ")
                            .append(argToString);
                    arguments[i][0] = paramType;
                    arguments[i][1] = argToString;
                    if(i < parameters.length-1){
                        prettyCall.append(", ");
                    }
                }
                shouldDetailThread.put(threadName, true);
            } else {
                prettyCall.append("JAVAFLAME_DETAILED_TOSTRING");
            }
            prettyCall.append(")");
            long entryTime = System.currentTimeMillis();
            onEnter(
                threadName,
                prettyCall.toString(),
                entryTime,
                arguments
            );
            return entryTime;
        } catch (Exception e){
            log(DEBUG, e.getMessage());
            return 0;
        }
    }

    public static String getTypeAsString(Object[] allArguments, int i, Parameter parameter) {
        String paramType;
        if (allArguments[i].getClass().isArray()){
            if (allArguments[i] instanceof boolean[]) {
                paramType = "boolean[]";
            } else if (allArguments[i]  instanceof byte[]) {
                paramType = "byte[]";
            } else if (allArguments[i]  instanceof char[]) {
                paramType = "char[]";
            } else if (allArguments[i]  instanceof double[]) {
                paramType = "double[]";
            } else if (allArguments[i]  instanceof float[]) {
                paramType = "float[]";
            } else if (allArguments[i]  instanceof int[]) {
                paramType = "int[]";
            } else if (allArguments[i]  instanceof short[]) {
                paramType = "short[]";
            } else if (allArguments[i]  instanceof long[]) {
                paramType = "long[]";
            } else {
                paramType = "Object[]";
            }
        } else {
            if ( FunctionCallRecorder.shouldPrintQualified ){
                paramType = parameter.getType().getName();
            } else {
                paramType = parameter.getType().getSimpleName();
            }
        }
        return paramType;
    }

    public static String getValueAsString(Object value) {
        if(value == null){
            return "null";
        }
        try {
            if (value.getClass().isArray()) {
                if (value instanceof boolean[]) {
                    return Arrays.toString((boolean[]) value);
                } else if (value instanceof byte[]) {
                    return Arrays.toString((byte[]) value);
                } else if (value instanceof char[]) {
                    return Arrays.toString((char[]) value);
                } else if (value instanceof double[]) {
                    return Arrays.toString((double[]) value);
                } else if (value instanceof float[]) {
                    return Arrays.toString((float[]) value);
                } else if (value instanceof int[]) {
                    return Arrays.toString((int[]) value);
                } else if (value instanceof short[]) {
                    return Arrays.toString((short[]) value);
                } else if (value instanceof long[]) {
                    return Arrays.toString((long[]) value);
                } else {
                    return Arrays.toString((Object[]) value);
                }
            } else {
                return value.toString();
            }
        } catch (Exception e) {
            return "ARG_TOSTRING_EXCEPTION "+e;
        }
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(
        @Advice.Enter long start,
        @Advice.Return(typing = Assigner.Typing.DYNAMIC) Object returnValueFromMethod
    ) {
        try{
            long exitTime = System.currentTimeMillis();
            final String threadName = Thread.currentThread().getName();

            String returnValue = null;

            // We avoid extracting detail when executing a toString for a parameter
            // or else we risk creating a stack overflow
            Boolean createDetails = shouldDetailThread.getOrDefault(threadName, true);
            if(createDetails){
                shouldDetailThread.put(threadName, false);
                try {
                    returnValue = getValueAsString(returnValueFromMethod);
                } catch (Exception e) {
                    returnValue = "RETURN_TOSTRING_EXCEPTION "+e;
                }
                shouldDetailThread.put(threadName, true);
            }
            String returnType;
            try {
                if (shouldPrintQualified) {
                    returnType = returnValueFromMethod.getClass().getName();
                } else {
                    returnType = returnValueFromMethod.getClass().getSimpleName();
                }
            } catch (NullPointerException np) {
                returnType = "void";
                if ( returnValueFromMethod == null ){
                    returnValue = "";
                }
            }
            onLeave(threadName, exitTime, new String[]{returnType, returnValue});
        } catch (Exception e) {
            log(DEBUG, e.getMessage());
        }
    }
}