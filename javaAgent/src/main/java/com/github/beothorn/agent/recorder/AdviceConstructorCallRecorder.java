package com.github.beothorn.agent.recorder;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Constructor;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;

public class AdviceConstructorCallRecorder {

    @Advice.OnMethodEnter
    public static void enter(
            @Advice.Origin Constructor<?> constructor,
            @Advice.AllArguments Object[] allArguments
    ) {
        System.out.println("CONSTRUCTOR!!!!!");
        try {
            FunctionCallRecorderWithValueCapturing.enterConstructor(constructor, allArguments);
        } catch (Exception e){
            e.printStackTrace();
            log(DEBUG, e.getMessage());
        }
    }
//    @Advice.OnMethodExit(onThrowable = Throwable.class)
//    public static void exit(
//    ) {
//        FunctionCallRecorderWithValueCapturing.exit(null);
//    }
}
