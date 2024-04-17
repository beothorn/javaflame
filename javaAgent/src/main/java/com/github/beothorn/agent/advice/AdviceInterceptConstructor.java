package com.github.beothorn.agent.advice;

import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.This;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

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

            // TODO: multithreading
            isRecording = false;
            try {
                methodToCall.invoke(null, self);
            } finally {
                isRecording = true;
            }
        } catch (Exception e){
            log(ERROR, "On intercept exit constructor Exception " + e);
            log(ERROR, "On exit constructor interceptor "+e.getMessage());
            log(ERROR, "On intercept calling '" +classFullName+"#"+method+"'");
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }

}
