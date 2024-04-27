package com.github.beothorn.agent.parser;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ElementMatcherFromExpression implements Assembler<ElementMatcherFromExpression>{

    private static final String RESERVED_KEYWORD_CONSTRUCTOR = "new";

    public static ElementMatcherFromExpression forExpression(String input) throws CompilationException {
        Deque<Token> tokens = Lexer.tokenize(input);
        ASTNode root = Parser.parse(tokens);
        return root.apply(new ElementMatcherFromExpression());
    }

    private static ElementMatcherFromExpression forClassMatcher(ElementMatcher.Junction<NamedElement> typeMatcher) {
        ElementMatcherFromExpression elementMatcherFromExpression = new ElementMatcherFromExpression();
        elementMatcherFromExpression.typeMatcher = typeMatcher;
        return elementMatcherFromExpression;
    }

    private static ElementMatcherFromExpression forMethodMatcher(ElementMatcher.Junction<MethodDescription> methodMatcher) {
        ElementMatcherFromExpression elementMatcherFromExpression = new ElementMatcherFromExpression();
        elementMatcherFromExpression.methodMatcher = methodMatcher;
        return elementMatcherFromExpression;
    }

    /**
     * All matchers pair for class and method.
     * This is a pair so function matchers are only applied to the right classes.
     */
    private final List<ClassAndMethodMatcher> matchers = new ArrayList<>();
    /**
     * This will match all classes on the expression, disregard the method.
     * This exists because the matching happens in two steps.
     * The first one is only for the class itself, and the second one is for the class and the method.
     * Only after matching this, the function matcher will be applied.
     */
    private ElementMatcher.Junction<NamedElement> typeMatcher;

    private ElementMatcher.Junction<MethodDescription> methodMatcher;

    public ElementMatcher.Junction<NamedElement> getClassMatcher(){
        return typeMatcher;
    }

    public List<ClassAndMethodMatcher> getClassAndMethodMatchers(){
        return matchers;
    }

    @Override
    public ElementMatcherFromExpression assembleDefaultMatcher(
        final Token token,
        final List<Flag> flags
    ) {
        if(token.value.equals(RESERVED_KEYWORD_CONSTRUCTOR)){
            ElementMatcherFromExpression elementMatcherFromExpression = new ElementMatcherFromExpression();
            elementMatcherFromExpression.methodMatcher = isConstructor().and(methodMatcher);
            return elementMatcherFromExpression;
        }
        boolean isMethod = flags.contains(Flag.METHOD_EXPRESSION);
        if(isMethod) return forMethodMatcher(nameContains(token.value));
        return forClassMatcher(nameContains(token.value));
    }

    @Override
    public ElementMatcherFromExpression assembleDeclaredMatcher(
        final Token functionToken,
        final String stringArgument,
        final List<Flag> flags
    ) throws CompilationException {
        boolean isMethod = flags.contains(Flag.METHOD_EXPRESSION);
        String function = functionToken.value;
        if (function.equals("named")) {
            if(isMethod) return forMethodMatcher(named(stringArgument));
            return forClassMatcher(named(stringArgument));
        }
        if (function.equals("namedIgnoreCase")) {
            if(isMethod) return forMethodMatcher(namedIgnoreCase(stringArgument));
            return forClassMatcher(namedIgnoreCase(stringArgument));
        }
        if (function.equals("nameStartsWith")) {
            if(isMethod) return forMethodMatcher(nameStartsWith(stringArgument));
            return forClassMatcher(nameStartsWith(stringArgument));
        }
        if (function.equals("nameStartsWithIgnoreCase")) {
            if(isMethod) return forMethodMatcher(nameStartsWithIgnoreCase(stringArgument));
            return forClassMatcher(nameStartsWithIgnoreCase(stringArgument));
        }
        if (function.equals("nameEndsWith")) {
            if(isMethod) return forMethodMatcher(nameEndsWith(stringArgument));
            return forClassMatcher(nameEndsWith(stringArgument));
        }
        if (function.equals("nameEndsWithIgnoreCase")) {
            if(isMethod) return forMethodMatcher(nameEndsWithIgnoreCase(stringArgument));
            return forClassMatcher(nameEndsWithIgnoreCase(stringArgument));
        }
        if (function.equals("nameContains")) {
            if(isMethod) return forMethodMatcher(nameContains(stringArgument));
            return forClassMatcher(nameContains(stringArgument));
        }
        if (function.equals("nameContainsIgnoreCase")) {
            if(isMethod) return forMethodMatcher(nameContainsIgnoreCase(stringArgument));
            return forClassMatcher(nameContainsIgnoreCase(stringArgument));
        }
        if (function.equals("nameMatches")) {
            if(isMethod) return forMethodMatcher(nameMatches(stringArgument));
            return forClassMatcher(nameMatches(stringArgument));
        }

        throw new CompilationException("Unknown function '"+function+
                "' Should be one of these:[named, namedIgnoreCase, nameStartsWith, nameStartsWithIgnoreCase, " +
                "nameEndsWith, nameEndsWithIgnoreCase, nameContains, nameContainsIgnoreCase, nameMatches]");
    }

    @Override
    public ElementMatcherFromExpression assemble(
        final Token token,
        final List<ElementMatcherFromExpression> args,
        final List<Flag> flags
    ) {
        boolean isMethod = flags.contains(Flag.METHOD_EXPRESSION);
        ElementMatcherFromExpression elementMatcherFromExpression = new ElementMatcherFromExpression();
        switch (token.type) {
            case OPERATOR_OR:
                if(isMethod){
                    elementMatcherFromExpression.methodMatcher = args.get(0).methodMatcher.or(args.get(1).methodMatcher);
                    return elementMatcherFromExpression;
                }
                elementMatcherFromExpression.typeMatcher = args.get(0).typeMatcher.or(args.get(1).typeMatcher);
                elementMatcherFromExpression.matchers.addAll(args.get(0).matchers);
                elementMatcherFromExpression.matchers.addAll(args.get(1).matchers);
                return elementMatcherFromExpression;
            case OPERATOR_AND:
                if(isMethod){
                    elementMatcherFromExpression.methodMatcher = args.get(0).methodMatcher.and(args.get(1).methodMatcher);
                    return elementMatcherFromExpression;
                }
                elementMatcherFromExpression.typeMatcher = args.get(0).typeMatcher.and(args.get(1).typeMatcher);
                elementMatcherFromExpression.matchers.addAll(args.get(0).matchers);
                elementMatcherFromExpression.matchers.addAll(args.get(1).matchers);
                return elementMatcherFromExpression;
            case OPERATOR_NOT:
                if(isMethod){
                    elementMatcherFromExpression.methodMatcher = not(args.get(0).methodMatcher);
                    return elementMatcherFromExpression;
                }
                elementMatcherFromExpression.typeMatcher = not(args.get(0).typeMatcher);
                elementMatcherFromExpression.matchers.addAll(args.get(0).matchers);
                return elementMatcherFromExpression;
            case FUNCTION_MATCHER_VALUE:
                // Only the first argument is a class matcher, second one is a method matcher
                elementMatcherFromExpression.typeMatcher = args.get(0).typeMatcher;
                elementMatcherFromExpression.matchers.add(
                    ClassAndMethodMatcher.matcher(
                        args.get(0).typeMatcher,
                        args.get(1).methodMatcher
                    )
                );
                return elementMatcherFromExpression;
        }
        throw new RuntimeException("Can not assemble");
    }
}
