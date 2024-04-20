package com.github.beothorn.agent.advice;

import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Origin;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

/***
 * This advice is supposed to be injected on methods.
 * It will call the recorder to record the method call on the next snapshot.
 * This advice does not get the capture the values of the parameters.
 */
public class AdviceFunctionCallRecorder {
    @OnMethodEnter
    public static void enter(
        @Origin Method method,
        @AllArguments Object[] allArguments
    ) {
        try {
            FunctionCallRecorder.enterFunction(method);
        } catch (Exception e){
            log(ERROR, "On enter function "+e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
    @OnMethodExit
    public static void exit() {
        try {
            FunctionCallRecorder.exit();
        } catch (Exception e) {
            log(ERROR, "On exit function " + e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
}
