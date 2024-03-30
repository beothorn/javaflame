package com.github.beothorn.agent.advice;

import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Origin;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.ERROR;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;

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
