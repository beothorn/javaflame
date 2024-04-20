package com.github.beothorn.agent.recorder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.beothorn.agent.recorder.FunctionCallRecorder.*;

public class FunctionCallRecorderWithValueCapturing {

    public static final Map<String, Boolean> shouldDetailThread = new ConcurrentHashMap<>();

    public static void enterConstructor(
        Constructor<?> constructor,
        Object[] allArguments
    ) {
        String methodName = "new";
        String ownerClass = getClassNameFor(constructor);
        Parameter[] parameters = constructor.getParameters();
        String ownerClassFullName = constructor.getDeclaringClass().getName();

        enterExecution(
            allArguments,
            ownerClass,
            methodName,
            parameters,
            ownerClassFullName
        );
    }

    public static void enterFunction(
        Method method,
        Object[] allArguments
    ) {
        String methodName = method.getName();
        String ownerClass = getClassNameFor(method);
        Parameter[] parameters = method.getParameters();
        String ownerClassFullName = method.getDeclaringClass().getName();

        enterExecution(
            allArguments,
            ownerClass,
            methodName,
            parameters,
            ownerClassFullName
        );
    }

    private static void enterExecution(
        final Object[] allArguments,
        final String ownerClass,
        final String methodName,
        final Parameter[] parameters,
        final String ownerClassFullName
    ) {
        final String threadName = Thread.currentThread().getName();
        // We do not record calls while getting parameter values, because toString()
        // can call other methods which we are not interested on
        Boolean createDetails = shouldDetailThread.getOrDefault(threadName, true);
        if (!createDetails) {
            return;
        }

        StringBuilder prettyCall = new StringBuilder();
        prettyCall.append(ownerClass)
                .append(".")
                .append(methodName)
                .append("(");
        String[][] arguments = new String[parameters.length][2];

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
        prettyCall.append(")");
        long entryTime = System.currentTimeMillis();
        onEnter(
            threadName,
            prettyCall.toString(),
                ownerClassFullName,
                methodName,
            entryTime,
            arguments
        );
    }

    public static String getTypeAsString(
        Object[] allArguments,
        int i,
        Parameter parameter
    ) {
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

    public static void exit(
        Object returnValueFromMethod
    ) {
        final String threadName = Thread.currentThread().getName();
        // We avoid extracting detail when executing a toString for a parameter
        // or else we risk creating a stack overflow
        Boolean createDetails = shouldDetailThread.getOrDefault(threadName, true);
        if (!createDetails) {
            return;
        }

        long exitTime = System.currentTimeMillis();

        String returnValue;

        shouldDetailThread.put(threadName, false);
        try {
            returnValue = getValueAsString(returnValueFromMethod);
        } catch (Exception e) {
            returnValue = "RETURN_TOSTRING_EXCEPTION "+e;
        }
        shouldDetailThread.put(threadName, true);

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
    }
}