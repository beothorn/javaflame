package com.github.beothorn.agent.parser;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Deque;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ElementMatcherFromExpression implements Assembler<ElementMatcher.Junction<NamedElement>>{

    public static ElementMatcher.Junction<NamedElement> forExpression(String input) throws CompilationException {
        Deque<Token> tokens = Lexer.tokenize(input);
        ASTNode root = Parser.parse(tokens);
        return root.apply(new ElementMatcherFromExpression());
    }

    @Override
    public ElementMatcher.Junction<NamedElement> assembleDefaultMatcher(final Token token) {
        return nameContains(token.value);
    }

    @Override
    public ElementMatcher.Junction<NamedElement> assembleDeclaredMatcher(
        final Token functionToken,
        final String stringArgument
    ) throws CompilationException {
        String function = functionToken.value;
        if (function.equals("named")) {
            return named(stringArgument);
        }
        if (function.equals("namedIgnoreCase")) {
            return namedIgnoreCase(stringArgument);
        }
        if (function.equals("nameStartsWith")) {
            return nameStartsWith(stringArgument);
        }
        if (function.equals("nameStartsWithIgnoreCase")) {
            return nameStartsWithIgnoreCase(stringArgument);
        }
        if (function.equals("nameEndsWith")) {
            return nameEndsWith(stringArgument);
        }
        if (function.equals("nameEndsWithIgnoreCase")) {
            return nameEndsWithIgnoreCase(stringArgument);
        }
        if (function.equals("nameContains")) {
            return nameContains(stringArgument);
        }
        if (function.equals("nameContainsIgnoreCase")) {
            return nameContainsIgnoreCase(stringArgument);
        }
        if (function.equals("nameMatches")) {
            return nameMatches(stringArgument);
        }

        throw new CompilationException("Unknown function '"+function+
                "' Should be one of these:[named, namedIgnoreCase, nameStartsWith, nameStartsWithIgnoreCase, " +
                "nameEndsWith, nameEndsWithIgnoreCase, nameContains, nameContainsIgnoreCase, nameMatches]");
    }

    @Override
    public ElementMatcher.Junction<NamedElement> assemble(
        final Token token,
        final List<ElementMatcher.Junction<NamedElement>> args
    ) {
        switch (token.type) {
            case OPERATOR_OR:
                return args.get(0).or(args.get(1));
            case OPERATOR_AND:
                return args.get(0).and(args.get(1));
            case OPERATOR_NOT:
                return not(args.get(0));
            case FUNCTION_MATCHER_VALUE:
                return args.get(0); // ignore function matcher, it will be evaluated later
            case FUNCTION_CALL:

        }
        throw new RuntimeException("Can not assemble");
    }
}
