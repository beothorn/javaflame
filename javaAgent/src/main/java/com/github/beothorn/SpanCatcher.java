package com.github.beothorn;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SpanCatcher {

    private static Method onEnter;
    private static Method onLeave;

    public static void callOnEnter(Method method){
        if(onEnter == null) {
            try {
                Class methodListener = getMethodListenerClass();
                onEnter = methodListener.getDeclaredMethod("onEnter", String.class);
                onEnter.setAccessible(true);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            onEnter.invoke(null, method.getName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void callOnLeave(Method method, long executionTime){
        if(onLeave == null) {
            try {
                Class methodListener = getMethodListenerClass();
                onLeave = methodListener.getDeclaredMethod("onLeave", String.class, Long.class);
                onLeave.setAccessible(true);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            onLeave.invoke(null, method.getName(), executionTime);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class getMethodListenerClass() throws ClassNotFoundException {
        Class methodListener = Class.forName("com.github.beothorn.flameServer.MethodListener");
        return methodListener;
    }

    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin Method method) {
        System.out.println("------->"+Thread.currentThread().getName());
        callOnEnter(method);
        return System.currentTimeMillis();
    }
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(
        @Advice.Enter long start,
        @Advice.Origin Method method
    ) {
        long executionTime = System.currentTimeMillis() - start;
        callOnLeave(method, executionTime);
    }
}