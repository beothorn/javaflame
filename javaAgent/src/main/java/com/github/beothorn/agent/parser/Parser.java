package com.github.beothorn.agent.parser;

import java.util.Deque;

import static com.github.beothorn.agent.parser.ASTNode.n;

public class Parser {

    public static ASTNode parse(final Deque<Token> tokens) {
        ASTNode result = null;
        while (tokens.peek() != null) {
            result = parseNext(tokens, result);
        }
        return result;
    }

    public static ASTNode parseUntilClose(final Deque<Token> tokens) {
        ASTNode result = null;
        while (tokens.peek() != null) {
            if (tokens.peek().type.equals(TokenType.CLOSE_PAREN)) {
                tokens.pop();
                return result;
            }
            result = parseNext(tokens, result);
        }
        throw new RuntimeException("Unclosed parenthesis");
    }

    private static ASTNode parseNext(final Deque<Token> tokens) {
        return parseNext(tokens, null);
    }

    private static ASTNode parseNext(final Deque<Token> tokens, ASTNode resultSoFar) {
        Token token = tokens.pop();
        TokenType type = token.type;
        switch (type) {
            case STRING_VALUE:
                if (resultSoFar != null) {
                    throw new RuntimeException("Can't have two consecutive strings or strings without logic");
                }
                return n(token);
            case FUNCTION_CALL:
                break;
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
            case OPEN_PAREN:
                return parseUntilClose(tokens);
            case CLOSE_PAREN:
                throw new RuntimeException("Unexpected close parenthesis.");
        }
        throw new RuntimeException("NOT IMPLEMENTED");
    }
}
