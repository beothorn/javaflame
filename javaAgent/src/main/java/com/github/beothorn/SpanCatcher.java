package com.github.beothorn;

import net.bytebuddy.asm.Advice;

public class SpanCatcher {
    @Advice.OnMethodEnter
    public static long enter() {
        return System.currentTimeMillis();
    }
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(
        @Advice.Enter long start,
        @Advice.Origin String origin
    ) {
        long executionTime = System.currentTimeMillis() - start;
        System.out.println(origin + " took " + executionTime + " to execute");
    }
}