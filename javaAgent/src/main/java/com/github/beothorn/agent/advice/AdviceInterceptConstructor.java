package com.github.beothorn.agent.advice;

import com.github.beothorn.agent.transformer.ConstructorInterceptor;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.This;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.ERROR;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;

public class AdviceInterceptConstructor {

    @OnMethodExit
    public static void exit(
            @This Object self
    ) {
        try{
            Class<?> clazz = Class.forName(ConstructorInterceptor.classFullName);
            Method method = clazz.getMethod(ConstructorInterceptor.method, Object.class);
            method.invoke(null, self);
        } catch (Exception e){
            log(ERROR, "On exit constructor interceptor "+e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }

}
