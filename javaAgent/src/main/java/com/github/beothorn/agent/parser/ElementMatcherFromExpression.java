package com.github.beothorn.agent.parser;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Deque;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.not;

public class ElementMatcherFromExpression implements Assembler<ElementMatcher.Junction<NamedElement>>{

    public static ElementMatcher.Junction<NamedElement> forExpression(String input) throws CompilationException {
        Deque<Token> tokens = Lexer.tokenize(input);
        ASTNode root = Parser.parse(tokens);
        return root.apply(new ElementMatcherFromExpression());
    }

    @Override
    public ElementMatcher.Junction<NamedElement> assemble(final Token token) {
        return nameContains(token.value);
    }

    @Override
    public ElementMatcher.Junction<NamedElement> assemble(final Token token, final List<ElementMatcher.Junction<NamedElement>> args) {
        switch (token.type) {
            case OPERATOR_OR:
                return args.get(0).or(args.get(1));
            case OPERATOR_AND:
                return args.get(0).and(args.get(1));
            case OPERATOR_NOT:
                return not(args.get(0));
        }
        throw new RuntimeException("Can not assemble");
    }
}
