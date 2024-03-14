package com.github.beothorn.agent.parser;

import java.util.Deque;

import static com.github.beothorn.agent.parser.ASTNode.n;
import static com.github.beothorn.agent.parser.TokenType.*;

public class Parser {

    public static ASTNode parse(final Deque<Token> tokens) throws CompilationException {
        ASTNode result = null;
        while (tokens.peek() != null) {
            result = parseNext(tokens, result);
        }
        return result;
    }

    public static ASTNode parseUntilClose(final Deque<Token> tokens) throws CompilationException {
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

    private static ASTNode parseNext(final Deque<Token> tokens, ASTNode resultSoFar) throws CompilationException {
        Token token = tokens.pop();
        Token peek = tokens.peek();
        TokenType type = token.type;
        switch (type) {
            case STRING_VALUE:
                if (resultSoFar != null) {
                    throw new CompilationException("Can't have two consecutive strings or strings without logic");
                }
                if (peek != null && peek.type.equals(FUNCTION_MATCHER_VALUE)) {
                    return n(
                        token,
                        parseNext(tokens)
                    );
                }
                return n(token);
            case FUNCTION_CALL:
                throw new RuntimeException("NOT IMPLEMENTED");
            case OPERATOR_AND:
            case OPERATOR_OR:
                return n(
                        token,
                        resultSoFar,
                        parseNext(tokens)
                );
            case OPERATOR_NOT:
                return n(
                        token,
                        parseNext(tokens)
                );
            case FUNCTION_MATCHER_VALUE:
                if (peek == null) {
                    throw new CompilationException("Illegal end of expression #.");
                }
                if (peek.type.equals(STRING_VALUE)) {
                    return n(tokens.pop());
                }
                if (peek.type.equals(OPEN_PAREN)) {
                    return n(token, parseUntilClose(tokens));
                }
                if (peek.type.equals(FUNCTION_CALL)) {
                    throw new RuntimeException("NOT IMPLEMENTED");
                }
                throw new CompilationException("Unexpected symbol after #.");
            case OPEN_PAREN:
                return parseUntilClose(tokens);
            case CLOSE_PAREN:
                throw new CompilationException("Unexpected close parenthesis.");
        }
        throw new RuntimeException("NOT IMPLEMENTED");
    }
}
