package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Test;

import static com.github.beothorn.agent.parser.Token.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTest {

    @Test
    void singleName() throws CompilationException {
        Lexer foo = new Lexer("foo");
        assertEquals(foo.tokens.size(), 1);
        assertEquals(foo.tokens.pop(), string("foo"));
    }

    @Test
    void singleNameWithSeparator() throws CompilationException {
        Lexer foo = new Lexer("foo.bar");
        assertEquals(foo.tokens.pop(), string("foo.bar"));
    }

    @Test
    void happyDayAnd() throws CompilationException  {
        Lexer foo = new Lexer("foo.bar&&baz");
        assertArrayEquals(new Token[]{
                string("foo.bar"), and(), string("baz")
            },
            foo.tokens.toArray()
        );
    }

    @Test
    void happyDayOr() throws CompilationException  {
        Lexer foo = new Lexer("foo.bar||baz");
        assertArrayEquals(new Token[]{
                string("foo.bar"), or(), string("baz")
            },
            foo.tokens.toArray()
        );
    }

    @Test
    void happyDayNot() throws CompilationException  {
        Lexer foo = new Lexer("!foo.bar");
        assertArrayEquals(new Token[]{
                not(), string("foo.bar")
            },
            foo.tokens.toArray()
        );
        assertEquals(foo.tokens.pop(), not());
        assertEquals(foo.tokens.pop(), string("foo.bar"));
    }

    @Test
    void happyDayParenthesis() throws CompilationException  {
        Lexer foo = new Lexer("(foo.bar)");
        assertArrayEquals(new Token[]{
                openParen(), string("foo.bar"), closeParen()
            },
            foo.tokens.toArray()
        );
    }

    @Test
    void happyDayFunctionCall() throws CompilationException  {
        Lexer foo = new Lexer("(endsWith(foo.bar))");
        assertArrayEquals(new Token[]{
                openParen(), function("endsWith"), openParen(), string("foo.bar"), closeParen(), closeParen()
            },
            foo.tokens.toArray()
        );
    }

    @Test
    void happyDayComplexInput() throws CompilationException  {
        Lexer foo = new Lexer("aaa&&bbb||!(endsWith(foo.bar)||abc)");
        assertArrayEquals(new Token[]{
                string("aaa"), and(), string("bbb"), or(), not(),
                openParen(), function("endsWith"), openParen(), string("foo.bar"), closeParen(),
                or(), string("abc"), closeParen()
            },
            foo.tokens.toArray()
        );
    }

    @Test
    void spaceException() {
        try {
            new Lexer("foo.bar && baz");
        } catch (CompilationException e) {
            assertEquals(e.getMessage(), "Invalid input: Spaces are not allowed: \"foo.bar[ ]&& baz\"");
        }
    }

    @Test
    void incompleteAndException() {
        try {
            new Lexer("foo.bar&baz");
        } catch (CompilationException e) {
            assertEquals(e.getMessage(), "Invalid input: Expected '&&' after '&': \"foo.bar[&]baz\"");
        }
    }

    @Test
    void incompleteOrException() {
        try {
            new Lexer("foo.bar|baz");
        } catch (CompilationException e) {
            assertEquals(e.getMessage(), "Invalid input: Expected '||' after '|': \"foo.bar[|]baz\"");
        }
    }

    @Test
    void wrongPlacedNot() {
        try {
            new Lexer("foo.bar!baz");
        } catch (CompilationException e) {
            assertEquals(e.getMessage(), "Invalid input: ! can appear only on start of string: \"foo.bar[!]baz\"");
        }
    }

}