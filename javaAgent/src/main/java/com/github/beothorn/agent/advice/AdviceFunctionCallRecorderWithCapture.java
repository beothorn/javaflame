package com.github.beothorn.agent.advice;

import com.github.beothorn.agent.recorder.FunctionCallRecorderWithValueCapturing;
import net.bytebuddy.asm.Advice.*;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

/***
 * This advice is supposed to be injected on methods.
 * It will call the recorder to record the method call on the next snapshot.
 * This advice capture the values of the parameters.
 */
public class AdviceFunctionCallRecorderWithCapture {
    @OnMethodEnter
    public static void enter(
        @Origin Method method,
        @AllArguments Object[] allArguments
    ) {
        try {
            FunctionCallRecorderWithValueCapturing.enterFunction(method, allArguments);
        } catch (Exception e){
            log(ERROR, "On enter function");
            log(ERROR, e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
    @OnMethodExit(onThrowable = Throwable.class)
    public static void exit(
        @Return(typing = Assigner.Typing.DYNAMIC) Object returnValueFromMethod
    ) {
        try {
            FunctionCallRecorderWithValueCapturing.exit(returnValueFromMethod);
        } catch (Exception e) {
            log(ERROR, "On exit function on function call recorder advice with capturing");
            log(ERROR, e.getMessage());
            log(DEBUG, e.toString());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
}
