package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Test;

import java.util.Deque;

import static com.github.beothorn.agent.parser.Token.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTest {

    @Test
    void singleName() throws CompilationException {
        Deque<Token> tokens = Lexer.tokenize("tokens");
        assertEquals(tokens.size(), 1);
        assertEquals(tokens.pop(), string("tokens"));
    }

    @Test
    void singleNameWithSeparator() throws CompilationException {
        Deque<Token> tokens = Lexer.tokenize("tokens.bar");
        assertEquals(tokens.pop(), string("tokens.bar"));
    }

    @Test
    void happyDayAnd() throws CompilationException  {
        Deque<Token> tokens = Lexer.tokenize("foo.bar&&baz");
        assertArrayEquals(new Token[]{
                string("foo.bar"), and(), string("baz")
            },
            tokens.toArray()
        );
    }

    @Test
    void happyDayOr() throws CompilationException  {
        Deque<Token> tokens = Lexer.tokenize("tokens.bar||baz");
        assertArrayEquals(new Token[]{
                string("tokens.bar"), or(), string("baz")
            },
            tokens.toArray()
        );
    }

    @Test
    void happyDayNot() throws CompilationException  {
        Deque<Token> tokens = Lexer.tokenize("!tokens.bar");
        assertArrayEquals(new Token[]{
                not(), string("tokens.bar")
            },
            tokens.toArray()
        );
        assertEquals(tokens.pop(), not());
        assertEquals(tokens.pop(), string("tokens.bar"));
    }

    @Test
    void happyDayParenthesis() throws CompilationException  {
        Deque<Token> tokens = Lexer.tokenize("(tokens.bar)");
        assertArrayEquals(new Token[]{
                openParen(), string("tokens.bar"), closeParen()
            },
            tokens.toArray()
        );
    }

    @Test
    void happyDayFunctionCall() throws CompilationException  {
        Deque<Token> tokens = Lexer.tokenize("(endsWith(tokens.bar))");
        assertArrayEquals(new Token[]{
                openParen(), function("endsWith"), openParen(), string("tokens.bar"), closeParen(), closeParen()
            },
            tokens.toArray()
        );
    }

    @Test
    void happyDayFunctionFilter() throws CompilationException  {
        Deque<Token> tokens = Lexer.tokenize("tokens.bar#funcBaz");
        assertArrayEquals(new Token[]{
                string("tokens.bar"), startFunction(), string("funcBaz")
            },
            tokens.toArray()
        );
    }

    @Test
    void happyDayComplexInput() throws CompilationException  {
        Deque<Token> tokens = Lexer.tokenize("aaa#f1&&bbb#(f1||endsWith(f2))||!(endsWith(tokens.bar)||abc)");
        assertArrayEquals(new Token[]{
                string("aaa"), startFunction(), string("f1"), and(), string("bbb"), startFunction(),
                openParen(), string("f1"), or(), function("endsWith"), openParen(), string("f2"),
                closeParen(), closeParen(), or(), not(),
                openParen(), function("endsWith"), openParen(), string("tokens.bar"), closeParen(),
                or(), string("abc"), closeParen()
            },
            tokens.toArray()
        );
    }

    @Test
    void spaceException() {
        try {
            Lexer.tokenize("foo.bar && baz");
        } catch (CompilationException e) {
            assertEquals(e.getMessage(), "Invalid input: Spaces are not allowed: \"foo.bar[ ]&& baz\"");
        }
    }

    @Test
    void incompleteAndException() {
        try {
            Lexer.tokenize("foo.bar&baz");
        } catch (CompilationException e) {
            assertEquals(e.getMessage(), "Invalid input: Expected '&&' after '&': \"foo.bar[&]baz\"");
        }
    }

    @Test
    void incompleteOrException() {
        try {
            Lexer.tokenize("foo.bar|baz");
        } catch (CompilationException e) {
            assertEquals(e.getMessage(), "Invalid input: Expected '||' after '|': \"foo.bar[|]baz\"");
        }
    }

    @Test
    void wrongPlacedNot() {
        try {
            Lexer.tokenize("foo.bar!baz");
        } catch (CompilationException e) {
            assertEquals(e.getMessage(), "Invalid input: ! can appear only on start of string: \"foo.bar[!]baz\"");
        }
    }

}