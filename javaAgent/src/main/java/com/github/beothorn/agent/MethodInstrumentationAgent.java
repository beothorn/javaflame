package com.github.beothorn.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class MethodInstrumentationAgent {

    public static void premain(
        String argument,
        Instrumentation instrumentation
    ) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("I am leaving bye bye!!!!");
            System.out.println(SpanCatcher.getFinalCallStack());
            System.out.println("Done!!!!");
        }));

        new AgentBuilder.Default()
            .type(ElementMatchers.not(ElementMatchers.nameContains("com.github.beothorn.agent")))
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
