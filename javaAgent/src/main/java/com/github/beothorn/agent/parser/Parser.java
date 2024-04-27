package com.github.beothorn.agent.parser;

import java.util.Deque;

import static com.github.beothorn.agent.parser.ASTNode.n;
import static com.github.beothorn.agent.parser.TokenType.*;

public class Parser {

    /***
     * Given a stack of tokens produced by the Lexer, compiles an AST.
     *
     * @param tokens The tokens to be processed
     * @return The final AST
     * @throws CompilationException In case of oopsies
     */
    public static ASTNode parse(final Deque<Token> tokens) throws CompilationException {
        ASTNode result = null;
        while (tokens.peek() != null) {
            result = parseNext(tokens, result);
        }
        return result;
    }

    private static ASTNode parseUntilClose(final Deque<Token> tokens) throws CompilationException {
        ASTNode result = null;
        while (tokens.peek() != null) {
            if (tokens.peek().type.equals(TokenType.CLOSE_PAREN)) {
                tokens.pop();
                return result;
            }
            result = parseNext(tokens, result);
        }
        throw new CompilationException("Unclosed parenthesis");
    }

    private static ASTNode parseNext(final Deque<Token> tokens) throws CompilationException {
        return parseNext(tokens, null);
    }

    private static ASTNode parseNext(
        final Deque<Token> tokens,
        final ASTNode resultSoFar
    ) throws CompilationException {
        Token token = tokens.pop();
        Token peek = tokens.peek();
        TokenType type = token.type;
        switch (type) {
            case STRING_VALUE:
                return parseString(resultSoFar, token);
            case FUNCTION_CALL:
                return parseFunctionCall(tokens, peek, token);
            case OPERATOR_AND:
            case OPERATOR_OR:
                return parseBinaryOperand(tokens, resultSoFar, peek, token);
            case FUNCTION_MATCHER_VALUE:
                return parseFunctionMatcher(tokens, resultSoFar, peek, token);
            case OPERATOR_NOT:
                return parseUnaryOperand(tokens, token);
            case OPEN_PAREN:
                return parseUntilClose(tokens);
            case CLOSE_PAREN:
                throw new CompilationException("Unexpected close parenthesis.");
        }
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    private static ASTNode parseFunctionMatcher(
        final Deque<Token> tokens,
        final ASTNode resultSoFar,
        final Token peek,
        final Token token
    ) throws CompilationException {
        Token nextToken;
        if (peek == null) {
            throw new CompilationException("No expression after function matcher start "+ token.value);
        }
        if(resultSoFar.containsMethodExpression()) {
            throw new CompilationException("Nested method matcher (##).\n" +
                    "You have a function matcher inside your function matcher.\n" +
                    "That happens for example in classA#funA||classB#funB.\n" +
                    "Here, the function matcher is (funA||classB#funB).\n" +
                    "It should be declared as (classA#funA)||(classB#funB).\n" +
                    "Try using parenthesis around (class#function).");
        }
        nextToken = tokens.pop();
        if (FUNCTION_CALL.equals(nextToken.type)) {
            return n(
                token,
                resultSoFar,
                flagNodeAndChildrenAsMethodMatcher(
                    parseFunctionCall(
                        tokens,
                        tokens.peek(),
                        nextToken
                    )
                )
            );
        }
        if (OPEN_PAREN.equals(nextToken.type)) {
            return n(
                token,
                resultSoFar,
                flagNodeAndChildrenAsMethodMatcher(parseUntilClose(tokens))
            );
        }
        if (OPERATOR_NOT.equals(nextToken.type)) {
            return n(
                token,
                resultSoFar,
                flagNodeAndChildrenAsMethodMatcher(parseUnaryOperand(tokens, nextToken))
            );
        }
        ASTNode functionMatcherNode = n(nextToken);
        return n(
            token,
            resultSoFar,
            flagNodeAndChildrenAsMethodMatcher(functionMatcherNode)
        );
    }

    private static ASTNode flagNodeAndChildrenAsMethodMatcher(final ASTNode node){
        node.setMethodExpression();
        flagNodeChildrenAsMethodMatcher(node.children);
        return node;
    }

    private static void flagNodeChildrenAsMethodMatcher(final ASTNode[] nodes){
        for (final ASTNode node : nodes) {
            flagNodeAndChildrenAsMethodMatcher(node);
        }
    }

    private static ASTNode parseUnaryOperand(final Deque<Token> tokens, final Token token) throws CompilationException {
        return n(
            token,
            parseNext(tokens)
        );
    }

    private static ASTNode parseBinaryOperand(final Deque<Token> tokens, final ASTNode resultSoFar, final Token peek, final Token token) throws CompilationException {
        if (peek == null) {
            throw new CompilationException("No expression after operand "+ token.value);
        }
        return n(
            token,
            resultSoFar,
            parseNext(tokens)
        );
    }

    private static ASTNode parseFunctionCall(final Deque<Token> tokens, final Token peek, final Token token) throws CompilationException {
        Token nextToken;
        if (peek == null) {
            throw new CompilationException("No expression after function start "+ token.value);
        }
        nextToken = tokens.pop();
        if (!OPEN_PAREN.equals(nextToken.type)) {
            throw new CompilationException("Open parenthesis required after function start "+ token.value+" but found "+nextToken.value);
        }
        return n(
            token,
            parseUntilClose(tokens)
        );
    }

    private static ASTNode parseString(final ASTNode resultSoFar, final Token token) throws CompilationException {
        if (resultSoFar != null) {
            throw new CompilationException("Can't have two consecutive strings or strings without logic");
        }
        return n(token);
    }
}
