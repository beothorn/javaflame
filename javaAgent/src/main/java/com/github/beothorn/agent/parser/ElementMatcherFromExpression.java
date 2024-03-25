package com.github.beothorn.agent.parser;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ElementMatcherFromExpression implements Assembler<ElementMatcherFromExpression>{

    public static ElementMatcherFromExpression forExpression(String input) throws CompilationException {
        Deque<Token> tokens = Lexer.tokenize(input);
        ASTNode root = Parser.parse(tokens);
        return root.apply(new ElementMatcherFromExpression());
    }

    private static ElementMatcherFromExpression forMatcher(ElementMatcher.Junction<NamedElement> typeMatcher) throws CompilationException {
        ElementMatcherFromExpression elementMatcherFromExpression = new ElementMatcherFromExpression();
        elementMatcherFromExpression.typeMatcher = typeMatcher;
        return elementMatcherFromExpression;
    }

    private List<ClassAndMethodMatcher> matchers = new ArrayList<>();
    private  ElementMatcher.Junction<NamedElement> typeMatcher;

    public ElementMatcher.Junction<NamedElement> getClassMatcher(){
        return typeMatcher;
    }

    public List<ClassAndMethodMatcher> getClassAndMethodMatchers(){
        return matchers;
    }

    @Override
    public ElementMatcherFromExpression assembleDefaultMatcher(final Token token) throws CompilationException {
        return forMatcher(nameContains(token.value));
    }

    @Override
    public ElementMatcherFromExpression assembleDeclaredMatcher(
        final Token functionToken,
        final String stringArgument
    ) throws CompilationException {
        String function = functionToken.value;
        if (function.equals("named")) {
            return forMatcher(named(stringArgument));
        }
        if (function.equals("namedIgnoreCase")) {
            return forMatcher(namedIgnoreCase(stringArgument));
        }
        if (function.equals("nameStartsWith")) {
            return forMatcher(nameStartsWith(stringArgument));
        }
        if (function.equals("nameStartsWithIgnoreCase")) {
            return forMatcher(nameStartsWithIgnoreCase(stringArgument));
        }
        if (function.equals("nameEndsWith")) {
            return forMatcher(nameEndsWith(stringArgument));
        }
        if (function.equals("nameEndsWithIgnoreCase")) {
            return forMatcher(nameEndsWithIgnoreCase(stringArgument));
        }
        if (function.equals("nameContains")) {
            return forMatcher(nameContains(stringArgument));
        }
        if (function.equals("nameContainsIgnoreCase")) {
            return forMatcher(nameContainsIgnoreCase(stringArgument));
        }
        if (function.equals("nameMatches")) {
            return forMatcher(nameMatches(stringArgument));
        }

        throw new CompilationException("Unknown function '"+function+
                "' Should be one of these:[named, namedIgnoreCase, nameStartsWith, nameStartsWithIgnoreCase, " +
                "nameEndsWith, nameEndsWithIgnoreCase, nameContains, nameContainsIgnoreCase, nameMatches]");
    }

    @Override
    public ElementMatcherFromExpression assemble(
        final Token token,
        final List<ElementMatcherFromExpression> args
    ) throws CompilationException {
        ElementMatcherFromExpression elementMatcherFromExpression = new ElementMatcherFromExpression();
        switch (token.type) {
            case OPERATOR_OR:
                elementMatcherFromExpression.typeMatcher = args.get(0).typeMatcher.or(args.get(1).typeMatcher);
                elementMatcherFromExpression.matchers.addAll(args.get(0).matchers);
                elementMatcherFromExpression.matchers.addAll(args.get(1).matchers);
                return elementMatcherFromExpression;
            case OPERATOR_AND:
                elementMatcherFromExpression.typeMatcher = args.get(0).typeMatcher.and(args.get(1).typeMatcher);
                elementMatcherFromExpression.matchers.addAll(args.get(0).matchers);
                elementMatcherFromExpression.matchers.addAll(args.get(1).matchers);
                return elementMatcherFromExpression;
            case OPERATOR_NOT:
                elementMatcherFromExpression.typeMatcher = not(args.get(0).typeMatcher);
                elementMatcherFromExpression.matchers.addAll(args.get(0).matchers);
                return elementMatcherFromExpression;
            case FUNCTION_MATCHER_VALUE:
                elementMatcherFromExpression.typeMatcher = args.get(0).typeMatcher;
                elementMatcherFromExpression.matchers.add(
                        ClassAndMethodMatcher.matcher(args.get(0).typeMatcher, args.get(1).typeMatcher)
                );
                return elementMatcherFromExpression;
        }
        throw new RuntimeException("Can not assemble");
    }
}
