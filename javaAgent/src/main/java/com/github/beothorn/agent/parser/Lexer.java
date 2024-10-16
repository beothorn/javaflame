package com.github.beothorn.agent.parser;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.github.beothorn.agent.parser.Token.*;

public class Lexer {

    /**
     * This Lexer will generate tokens for our string matcher.
     * For example given the expression:
     * {@code (foobar||endsWith(baz) && !qux)}
     * we would get the tokens:
     * {@code {LEFT_PAREN, STRING_VALUE("foobar"), OPERATOR_OR("||"), FUNCTION_CALL("endsWith"), LEFT_PAREN("())")
     * STRING_VALUE("baz"), OPERATOR_AND("&&"), OPERATOR_NOT("!"), RIGHT_PAREN(")")} }
     *
     * @throws CompilationException with clear error message
     *
     * @param input The expression to be tokenized
     */
    public static Deque<Token> tokenize(String input) throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>();
        int cursor = 0;
        while (cursor < input.length()) {
            cursor = readNextToken(tokens, input, cursor);
        }
        return tokens;
    }

    /***
     * Starting from cursor, reads the next token from input, adds it to tokens and return new cursor position.
     *
     * @param tokens The stack to accumulate new tokens
     * @param input The string expression to evaluate
     * @param cursor The cursor position to start evaluation
     * @return new cursor position.
     * @throws CompilationException
     */
    private static int readNextToken(Deque<Token> tokens, String input, int cursor) throws CompilationException {
        char currentChar = input.charAt(cursor);

        if(currentChar == ' ') {
            throw new CompilationException(cursor, input, "Invalid input: Spaces are not allowed");
        }

        if(currentChar == '(') {
            tokens.add(openParen());
            return cursor + 1;
        }

        if(currentChar == ')') {
            tokens.add(closeParen());
            return cursor + 1;
        }

        if(currentChar == '!') {
            tokens.add(not());
            return cursor + 1;
        }

        if(currentChar == '|') {
            if (cursor + 1 >= input.length() || input.charAt(cursor + 1) != '|') {
                throw new CompilationException(cursor, input, "Invalid input: Expected '||' after '|'");
            }
            tokens.add(or());
            return cursor + 2;
        }

        if(currentChar == '&') {
            if (cursor + 1 >= input.length() || input.charAt(cursor + 1) != '&') {
                throw new CompilationException(cursor, input, "Invalid input: Expected '&&' after '&'");
            }
            tokens.add(and());
            return cursor + 2;
        }

        if(currentChar == '#') {
            tokens.add(functionMatcher());
            return cursor + 1;
        }

        int stringOrFunctionCursor = cursor + 1;
        while (stringOrFunctionCursor < input.length()) {
            char currentWordChar = input.charAt(stringOrFunctionCursor);
            if(currentWordChar == ' ') {
                throw new CompilationException(stringOrFunctionCursor, input, "Invalid input: Spaces are not allowed");
            }
            if(currentWordChar == ')'
                    || currentWordChar == '&'
                    || currentWordChar == '|'
                    || currentWordChar == '#') {
                tokens.add(string(input.substring(cursor, stringOrFunctionCursor)));
                return stringOrFunctionCursor;
            }
            if(currentWordChar == '(') {
                tokens.add(function(input.substring(cursor, stringOrFunctionCursor)));
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

        tokens.add(string(input.substring(cursor, stringOrFunctionCursor)));
        return stringOrFunctionCursor;
    }
}
