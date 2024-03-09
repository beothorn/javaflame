package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.github.beothorn.agent.parser.ASTNode.n;
import static com.github.beothorn.agent.parser.Token.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @Test
    void simpleParse(){
        Deque<Token> tokens = new ArrayDeque<>(asList(
            string("foo")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(string("foo")),
            root
        );
    }
    @Test
    void simpleParseAnd(){
        Deque<Token> tokens = new ArrayDeque<>(asList(
                string("foo"),
                and(),
                string("bar")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                and(),
                n(string("foo")),
                n(string("bar"))
            ),
            root
        );
    }

    @Test
    void simpleParseOr(){
        Deque<Token> tokens = new ArrayDeque<>(asList(
            string("foo"),
            or(),
            string("bar")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                or(),
                n(string("foo")),
                n(string("bar"))
            ),
            root
        );
    }

    @Test
    void simpleParseNot(){
        Deque<Token> tokens = new ArrayDeque<>(asList(
            not(),
            string("foo")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                not(),
                n(string("foo"))
            ),
            root
        );
    }

    @Test
    void simpleParseManyOperators(){
        Deque<Token> tokens = new ArrayDeque<>(asList(
            string("foo"),
            and(),
            string("bar"),
            or(),
            string("baz")
        )); // false && true || false = false because left comparision is first = ((false && true) || false)
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                or(),
                n(
                    and(),
                    n(string("foo")),
                    n(string("bar"))
                ),
                n(string("baz"))
            ),
            root
        );
    }

    @Test
    void simpleParseParenthesis(){
        // foo&&(bar||baz)
        Deque<Token> tokens = new ArrayDeque<>(asList(
            string("foo"),
            and(),
            openParen(),
            string("bar"),
            or(),
            string("baz"),
            closeParen()
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                and(),
                n(string("foo")),
                n(
                    or(),
                    n(string("bar")),
                    n(string("baz"))
                )
            ),
            root
        );
    }

    @Test
    void doubleParenthesis(){
        // ((foo&&bar)||baz)
        Deque<Token> tokens = new ArrayDeque<>(asList(
            openParen(),
            openParen(),
            string("foo"),
            and(),
            string("bar"),
            closeParen(),
            or(),
            string("baz"),
            closeParen()
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                or(),
                n(
                    and(),
                    n(string("foo")),
                    n(string("bar"))
                ),
                n(string("baz"))
            ),
            root
        );
    }

    @Test
    void singleItemParenthesis(){
        // (foo&&(bar)||baz)
        Deque<Token> tokens = new ArrayDeque<>(asList(
                openParen(),
                string("foo"),
                and(),
                openParen(),
                string("bar"),
                closeParen(),
                or(),
                string("baz"),
                closeParen()
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                or(),
                n(
                        and(),
                        n(string("foo")),
                        n(string("bar"))
                ),
                n(string("baz"))
            ),
            root
        );
    }

    @Test
    void tripleParenthesis(){
        //((foo&&(bar)||(baz)))
        Deque<Token> tokens = new ArrayDeque<>(asList(
            openParen(),
            openParen(),
            string("foo"),
            and(),
            openParen(),
            string("bar"),
            closeParen(),
            or(),
            openParen(),
            string("baz"),
            closeParen(),
            closeParen(),
            closeParen()
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                or(),
                n(
                    and(),
                    n(string("foo")),
                    n(string("bar"))
                ),
                n(string("baz"))
            ),
            root
        );
    }
}