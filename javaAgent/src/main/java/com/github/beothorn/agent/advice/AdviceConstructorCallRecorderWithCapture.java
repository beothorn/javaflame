package com.github.beothorn.agent.advice;

import com.github.beothorn.agent.recorder.FunctionCallRecorderWithValueCapturing;
import net.bytebuddy.asm.Advice.*;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

public class AdviceConstructorCallRecorderWithCapture {

    @OnMethodEnter
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
    @OnMethodExit
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
