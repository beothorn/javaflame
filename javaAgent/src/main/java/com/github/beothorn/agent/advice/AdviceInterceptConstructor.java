package com.github.beothorn.agent.advice;

import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.This;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

/***
 * This advice is supposed to be injected on constructor.
 * It will call the interceptor method passing the constructor call.
 * This advice ois injected at the end of the constructor so the new instance
 * is already created.
 * Note: This advice will be replaced by AdviceInterceptConstructorMethod when the expression parser
 * supports constructor expression.
 */
public class AdviceInterceptConstructor {

    public static String classFullName;
    public static String method;
    public static Method methodToCall;
    public static boolean isRecording = true;

    @OnMethodExit
    public static void exit(
        @This Object self
    ) {
        try{
            if (methodToCall != null) {
                methodToCall.invoke(null, self);
                return;
            }
            Class<?> clazz = Class.forName(classFullName);
            methodToCall = clazz.getMethod(method, Object.class);
            invoke(self);
        } catch (Exception e){
            log(ERROR, "On intercept exit constructor Exception " + e);
            log(ERROR, "On exit constructor interceptor "+e.getMessage());
            log(ERROR, "On intercept calling '" +classFullName+"#"+method+"'");
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }

    synchronized public static void invoke(Object self) throws IllegalAccessException, InvocationTargetException {
        isRecording = false;
        try {
            methodToCall.invoke(null, self);
        } finally {
            isRecording = true;
        }
    }

}
