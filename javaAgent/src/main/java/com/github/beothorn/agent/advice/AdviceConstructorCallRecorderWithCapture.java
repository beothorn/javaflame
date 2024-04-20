package com.github.beothorn.agent.advice;

import com.github.beothorn.agent.recorder.FunctionCallRecorderWithValueCapturing;
import net.bytebuddy.asm.Advice.*;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

/***
 * This advice is supposed to be injected on constructor.
 * It will call the recorder to record the constructor call on the next snapshot.
 * This advice capture the values of all parameters.
 */
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
            // The new instance is considered as the return value of the constructor.
            FunctionCallRecorderWithValueCapturing.exit(self);
        } catch (Exception e){
            log(ERROR, "On exit constructor "+e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
}
