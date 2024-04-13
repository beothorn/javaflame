package com.github.beothorn.agent.advice;

import com.github.beothorn.agent.recorder.FunctionCallRecorderWithValueCapturing;
import net.bytebuddy.asm.Advice.*;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

public class AdviceFunctionCallRecorderWithCapture {
    @OnMethodEnter
    public static void enter(
        @Origin Method method,
        @AllArguments Object[] allArguments
    ) {
        try {
            FunctionCallRecorderWithValueCapturing.enterFunction(method, allArguments);
        } catch (Exception e){
            log(ERROR, "On enter function "+e.getMessage());
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
            log(ERROR, "On exit function " + e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
}
