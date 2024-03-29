package com.github.beothorn.agent.recorder;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;

import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.DEBUG;
import static com.github.beothorn.agent.MethodInstrumentationAgent.log;

public class AdviceFunctionCallRecorder {
    @Advice.OnMethodEnter
    public static void enter(
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] allArguments
    ) {
        try {
            FunctionCallRecorderWithValueCapturing.enterFunction(method, allArguments);
        } catch (Exception e){
            e.printStackTrace();
            log(DEBUG, e.getMessage());
        }
    }
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(
            @Advice.Return(typing = Assigner.Typing.DYNAMIC) Object returnValueFromMethod
    ) {
        FunctionCallRecorderWithValueCapturing.exit(returnValueFromMethod);
    }
}
