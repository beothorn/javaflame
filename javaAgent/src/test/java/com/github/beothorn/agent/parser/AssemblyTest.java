package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.beothorn.agent.parser.ASTNode.n;
import static com.github.beothorn.agent.parser.Token.*;

public class AssemblyTest {

    public static class StringAssembler implements Assembler {

        @Override
        public Object assemble(final Token token) {
            return token.value;
        }

        @Override
        public Object assemble(final Token token, final Object[] args) {
            switch (token.type) {
                case OPERATOR_OR:
                    return args[0] + " OR " + args[1];
                case OPERATOR_AND:
                    return args[0] + " AND " + args[1];
                case OPERATOR_NOT:
                    return "NOT " + args[0];
            }
            throw new RuntimeException("Can not assemble");
        }
    }

    @Test
    void happyDay(){
        ASTNode n = n(
            or(),
            n(
                and(),
                n(string("foo")),
                n(string("bar"))
            ),
            n(string("baz"))
        );
        String s = (String) n.apply(new StringAssembler());
        Assertions.assertEquals("foo AND bar OR baz", s);
    }
}
