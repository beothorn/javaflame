package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.beothorn.agent.parser.ASTNode.n;
import static com.github.beothorn.agent.parser.Token.*;

public class AssemblyTest {

    public static class StringAssembler implements Assembler<String> {

        @Override
        public String assembleDefaultMatcher(final Token token) {
            return token.value;
        }

        @Override
        public String assembleDeclaredMatcher(final Token functionToken, final String stringArgument) throws CompilationException {
            return functionToken.value+"("+stringArgument+")";
        }

        @Override
        public String assemble(final Token token, final List<String> args) {
            switch (token.type) {
                case OPERATOR_OR:
                    return args.get(0) + " OR " + args.get(1);
                case OPERATOR_AND:
                    return args.get(0) + " AND " + args.get(1);
                case OPERATOR_NOT:
                    return "NOT " + args.get(0);
            }
            throw new RuntimeException("Can not assemble");
        }
    }

    @Test
    void happyDay() throws CompilationException {
        ASTNode n = n(
            or(),
            n(
                and(),
                n(string("foo")),
                n(
                    function("endsWith"),
                    n(string("bar"))
                )
            ),
            n(string("baz"))
        );
        String s = n.apply(new StringAssembler());
        Assertions.assertEquals("foo AND endsWith(bar) OR baz", s);
    }
}
