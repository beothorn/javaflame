package com.github.beothorn.agent.recorder;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.asm.Advice.This;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.ERROR;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;

public class AdviceConstructorCallRecorder {

    @Advice.OnMethodEnter
    public static void enter(
            @Origin Constructor<?> constructor,
            @AllArguments Object[] allArguments
    ) {
        try {
            FunctionCallRecorderWithValueCapturing.enterConstructor(constructor, allArguments);
        } catch (Exception e){
            log(ERROR, "On enter constructor "+e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
    @Advice.OnMethodExit
    public static void exit(
            @This Object self
    ) {
        try{
            FunctionCallRecorderWithValueCapturing.exit(self);
        } catch (Exception e){
            log(ERROR, "On exit constructor "+e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
}
