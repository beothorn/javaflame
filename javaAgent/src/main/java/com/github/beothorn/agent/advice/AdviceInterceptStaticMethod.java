package com.github.beothorn.agent.advice;

import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Executable;
import java.util.Arrays;

import static com.github.beothorn.agent.logging.Log.LogLevel.DEBUG;
import static com.github.beothorn.agent.logging.Log.LogLevel.ERROR;
import static com.github.beothorn.agent.logging.Log.log;

/***
 * This advice is supposed to be injected on static methods.
 * It will call the interceptor method passing the method call.
 * This advice is injected at the end of the constructor so the new instance
 * is already created.
 */
public class AdviceInterceptStaticMethod {

    @OnMethodExit(onThrowable = Throwable.class)
    public static void exit(
        @Origin Executable methodCalled,
        @AllArguments Object[] allArguments,
        @Return(typing = Assigner.Typing.DYNAMIC) Object returnValueFromMethod
    ) {
        try {
            AdviceInterceptMethod.exit(
                methodCalled,
                null,
                allArguments,
                returnValueFromMethod
            );
        } catch (Exception e) {
            log(ERROR, "On intercept exit static function Exception " + e);
            log(ERROR, "On intercept exit static function " + e.getMessage());
            log(DEBUG, Arrays.toString(e.getStackTrace()));
        }
    }
}
