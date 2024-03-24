package com.github.beothorn.agent.parser;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.List;

public class MethodMatcher implements Assembler<MethodMatcher>{

    public static MethodMatcher forExpression(final String expression) {
        return null;
    }

    @Override
    public MethodMatcher assembleDefaultMatcher(final Token token) throws CompilationException {
        return null;
    }

    @Override
    public MethodMatcher assembleDeclaredMatcher(final Token functionToken, final String stringArgument) throws CompilationException {
        return null;
    }

    @Override
    public MethodMatcher assemble(final Token token, final List<MethodMatcher> args) throws CompilationException {
        return null;
    }

    public ElementMatcher.Junction<MethodDescription> matcherForCanonicalName(final String name) {
        return null;
    }
}
