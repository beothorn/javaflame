package com.github.beothorn.agent.advice;

import net.bytebuddy.asm.Advice.*;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

public class AdviceInterceptMethod {

    public static String classFullName;
    public static String method;
    private static Method methodToCall;

    @OnMethodExit(onThrowable = Throwable.class)
    public static void exit(
        @Origin Method methodCalled,
        @This Object self,
        @AllArguments Object[] allArguments,
        @Return(typing = Assigner.Typing.DYNAMIC) Object returnValueFromMethod
    ) {
        try {
            if (methodToCall != null) {
                methodToCall.invoke(null, allArguments);
                return;
            }
            Class<?> clazz = Class.forName(classFullName);
            methodToCall = clazz.getMethod(method, Object.class);
            methodToCall.invoke(
                null,
                self,
                methodCalled,
                allArguments,
                returnValueFromMethod
            );
        } catch (Exception e) {
            log(ERROR, "On intercept exit function " + e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }

}
