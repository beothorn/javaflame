package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.beothorn.agent.parser.ASTNode.n;
import static com.github.beothorn.agent.parser.Token.*;

class MethodMatcherTest {

    @Test
    void happyDay() throws CompilationException {
        // a.b.c#funA
        ASTNode n = n(
            functionMatcher(),
            n(string("a.b.c")),
            n(string("funA"))
        );
        MethodMatcher methodMatcher = n.apply(new MethodMatcher());
        Assertions.assertNotNull(methodMatcher.matcherForCanonicalName("a.b.c"));
        Assertions.assertNull(methodMatcher.matcherForCanonicalName("d.e.f"));
    }

}