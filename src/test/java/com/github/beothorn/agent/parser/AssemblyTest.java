package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.beothorn.agent.parser.ASTNode.n;
import static com.github.beothorn.agent.parser.Token.*;

public class AssemblyTest {

    public static class StringAssembler implements Assembler<String> {

        @Override
        public String assembleDefaultMatcher(
            final Token token,
            final List<Flag> flags
        ) {
            if(!flags.isEmpty()) return Arrays.toString(flags.toArray())+"=>{"+token.value+"}";
            return token.value;
        }

        @Override
        public String assembleDeclaredMatcher(
            final Token functionToken,
            final String stringArgument,
            final List<Flag> flags
        ){
            if(!flags.isEmpty()) return Arrays.toString(flags.toArray())+"=>{"+functionToken.value+"("+stringArgument+")}";
            return functionToken.value+"("+stringArgument+")";
        }

        @Override
        public String assemble(
            final Token token,
            final List<String> args,
            final List<Flag> flags
        ) {
            switch (token.type) {
                case OPERATOR_OR:
                    if(!flags.isEmpty()) return Arrays.toString(flags.toArray())+"=>{"+args.get(0) + " OR " + args.get(1)+"}";
                    return args.get(0) + " OR " + args.get(1);
                case OPERATOR_AND:
                    if(!flags.isEmpty()) return Arrays.toString(flags.toArray())+"=>{"+args.get(0) + " AND " + args.get(1)+"}";
                    return args.get(0) + " AND " + args.get(1);
                case OPERATOR_NOT:
                    if(!flags.isEmpty()) return Arrays.toString(flags.toArray())+"=>{NOT " + args.get(0)+"}";
                    return "NOT " + args.get(0);
                case  FUNCTION_MATCHER_VALUE:
                    if(!flags.isEmpty()) return Arrays.toString(flags.toArray())+"=>{"+"Class{"+args.get(0) + "}#Function{" + args.get(1)+"}}";
                    return "Class{"+args.get(0) + "}#Function{" + args.get(1)+"}";
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

    @Test
    void happyDayWithClassAnMethod() throws CompilationException {
        ASTNode methodMatcher = n(
                or(),
                n(string("functionX")),
                n(string("functionY"))
        );
        methodMatcher.setMethodExpression();
        ASTNode n = n(
            functionMatcher(),
            n(
                or(),
                n(string("classX")),
                n(string("classY"))
            ),
                methodMatcher
        );
        String s = n.apply(new StringAssembler());
        Assertions.assertEquals("Class{classX OR classY}#Function{[METHOD_EXPRESSION]=>{functionX OR functionY}}", s);
    }
}
