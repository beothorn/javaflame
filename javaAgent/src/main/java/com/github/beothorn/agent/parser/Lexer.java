package com.github.beothorn.agent.parser;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.github.beothorn.agent.parser.Token.*;

public class Lexer {
    Deque<Token> tokens;

    /**
     * This Lexer will generate tokens for our string matcher.
     * For example given the expression:
     * (foobar||endsWith(baz) && !qux)
     * we would get the tokens:
     * {LEFT_PAREN, STRING_VALUE("foobar"), OPERATOR_OR("||"), FUNCTION_CALL("endsWith"), LEFT_PAREN("())")
     * STRING_VALUE("baz"), OPERATOR_AND("&&"), OPERATOR_NOT("!"), RIGHT_PAREN(")")}
     *
     * @throws Exception with clear error message
     *
     * @param input
     */
    public Lexer(String input) throws CompilationException {
        tokens = new ArrayDeque<>();
        int cursor = 0;
        while (cursor < input.length()) {
            cursor = readNextToken(input, cursor);
        }
    }

    private int readNextToken(String input, int cursor) throws CompilationException {
        char currentChar = input.charAt(cursor);

        if(currentChar == ' ') {
            throw new CompilationException(cursor, input, "Invalid input: Spaces are not allowed");
        }

        if(currentChar == '(') {
            add(openParen());
            return cursor + 1;
        }

        if(currentChar == ')') {
            add(closeParen());
            return cursor + 1;
        }

        if(currentChar == '!') {
            add(not());
            return cursor + 1;
        }

        if(currentChar == '|') {
            if (cursor + 1 >= input.length() || input.charAt(cursor + 1) != '|') {
                throw new CompilationException(cursor, input, "Invalid input: Expected '||' after '|'");
            }
            add(or());
            return cursor + 2;
        }

        if(currentChar == '&') {
            if (cursor + 1 >= input.length() || input.charAt(cursor + 1) != '&') {
                throw new CompilationException(cursor, input, "Invalid input: Expected '&&' after '&'");
            }
            add(and());
            return cursor + 2;
        }

        int stringOrFunctionCursor = cursor + 1;
        while (stringOrFunctionCursor < input.length()) {
            char currentWordChar = input.charAt(stringOrFunctionCursor);
            if(currentWordChar == ' ') {
                throw new CompilationException(stringOrFunctionCursor, input, "Invalid input: Spaces are not allowed");
            }
            if(currentWordChar == ')' || currentWordChar == '&' || currentWordChar == '|') {
                add(string(input.substring(cursor, stringOrFunctionCursor)));
                return stringOrFunctionCursor;
            }
            if(currentWordChar == '(') {
                add(function(input.substring(cursor, stringOrFunctionCursor)));
                return stringOrFunctionCursor;
            }
            if(currentWordChar == '!') {
                throw new CompilationException(
                    stringOrFunctionCursor,
                    input,
                    "Invalid input: ! can appear only on start of string"
                );
            }
            stringOrFunctionCursor++;
        }

        add(string(input.substring(cursor, stringOrFunctionCursor)));
        return stringOrFunctionCursor;
    }

    private boolean add(final Token t) {
        return tokens.add(t);
    }
}
