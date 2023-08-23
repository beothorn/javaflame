package com.github.beothorn;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class MethodInstrumentationAgent {

    public static void premain(
        String argument,
        Instrumentation instrumentation
    ) {
        new AgentBuilder.Default()
            .type(ElementMatchers.not(ElementMatchers.nameContains("com.github.beothorn.flameServer")))
            .transform(
                (
                    builder,
                    typeDescription,
                    classLoader,
                    module,
                    protectionDomain
                ) ->
                builder.visit(Advice.to(SpanCatcher.class).on(ElementMatchers.isMethod()))
            )
            .installOn(instrumentation);
    }
}
