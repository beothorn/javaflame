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
        Advice advice = Advice.to(SpanCatcher.class);
        new AgentBuilder.Default()
            .type(ElementMatchers.any())
            .transform(
                (
                    builder,
                    typeDescription,
                    classLoader,
                    module,
                    protectionDomain
                ) ->
                builder.visit(advice.on(ElementMatchers.isMethod()))
            )
            .installOn(instrumentation);
    }
}
