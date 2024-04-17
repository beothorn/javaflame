package com.github.beothorn.agent.advice;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.This;

import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

public class AdviceInterceptConstructorMethod {

    public static String classFullName;
    public static String method;
    public static Method methodToCall;

    public static boolean isRecording = true;

    @OnMethodExit
    public static void exit(
        @This Object self,
        @Advice.Origin Executable methodCalled,
        @AllArguments Object[] allArguments
    ) {
        try {
            if (!isRecording) return;
            if (methodToCall != null) {
                invoke(methodCalled, self, allArguments, self);
                return;
            }
            Class<?> clazz = Class.forName(classFullName);
            methodToCall = clazz.getMethod(
                method,
                Object.class,
                Executable.class,
                Object[].class,
                Object.class
            );

            invoke(methodCalled, self, allArguments, self);
        } catch (Exception e) {
            log(ERROR, "On intercept exit function Exception " + e);
            log(ERROR, "On intercept exit function " + e.getMessage());
            log(ERROR, "On intercept calling '" +classFullName+"#"+method+"'");
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }

    synchronized public static void invoke(
            final Executable methodCalled,
            final Object self,
            final Object[] allArguments,
            final Object returnValueFromMethod
    ) throws IllegalAccessException, InvocationTargetException {
        // TODO: multithread
        isRecording = false;
        try {
            methodToCall.invoke(
                null,
                self,
                methodCalled,
                allArguments,
                returnValueFromMethod
            );
        }finally {
            isRecording = true;
        }
    }

}
