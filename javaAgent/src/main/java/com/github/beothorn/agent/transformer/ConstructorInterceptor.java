package com.github.beothorn.agent.transformer;

import com.github.beothorn.agent.advice.AdviceInterceptConstructor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.isConstructor;

public class ConstructorInterceptor implements AgentBuilder.Transformer{

    public static String classFullName;
    public static String method;

    public ConstructorInterceptor(final String constructorInterceptorToCall) {
        String[] split = constructorInterceptorToCall.split("#");
        classFullName = split[0];
        method = split[1];
    }

    public DynamicType.Builder<?> transform(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader ignoredClassLoader,
            JavaModule ignoredModule
    ) {
        return getBuilder(builder, typeDescription);
    }

    @Override
    public DynamicType.Builder<?> transform(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain
    ) {
        return getBuilder(builder, typeDescription);
    }

    private DynamicType.Builder<?> getBuilder(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription
    ) {
        return builder.visit(Advice.to(AdviceInterceptConstructor.class).on(isConstructor()));
    }
}
