package com.github.beothorn.agent.advice;

import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Origin;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

/***
 * This advice is supposed to be injected on constructor.
 * It will call the recorder to record the constructor call on the next snapshot.
 * This advice does not get the capture the values of the parameters.
 */
public class AdviceConstructorCallRecorder {

    @OnMethodEnter
    public static void enter(
        @Origin Constructor<?> constructor
    ) {
        try {
            FunctionCallRecorder.enterConstructor(constructor);
        } catch (Exception e){
            log(ERROR, "On enter constructor "+e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
    @OnMethodExit
    public static void exit() {
        try{
            FunctionCallRecorder.exit();
        } catch (Exception e){
            log(ERROR, "On exit constructor "+e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
}
