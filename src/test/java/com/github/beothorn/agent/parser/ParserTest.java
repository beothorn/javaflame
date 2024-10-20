package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

import static com.github.beothorn.agent.parser.ASTNode.n;
import static com.github.beothorn.agent.parser.Token.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParserTest {

    @Test
    void simpleParse() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(Collections.singletonList(
                string("foo")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(string("foo")),
            root
        );
    }

    @Test
    void simpleParseWithFunction() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // foo#bar
            string("foo"),
            functionMatcher(),
            string("bar")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                functionMatcher(),
                n(string("foo")),
                n(string("bar"))
            ),
            root
        );
    }

    @Test
    void simpleParseWithFunctionNegated() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // foo#!bar
            string("foo"),
            functionMatcher(),
            not(),
            string("bar")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                functionMatcher(),
                n(string("foo")),
                n(
                    not(),
                    n(string("bar"))
                )
            ),
            root
        );
    }

    @Test
    void simpleParseWithFunctionAnFunctionCallMatcher() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // foo#endsWith(bar)
            string("foo"),
            functionMatcher(),
            function("endsWith"),
            openParen(),
            string("bar"),
            closeParen()
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                functionMatcher(),
                n(
                    string("foo")
                ),
                n(
                    function("endsWith"),
                    n(string("bar"))
                )
            ),
            root
        );
    }

    @Test
    void simpleParseWithTwoValuesWithOneFunction() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // Tricky, one could interpret this two ways, and applied to function or another class name
            // but if it was applied to the function we would need a terminator for the function matcher
            // If we want it, we can use parenthesis (see other tests)
            // foo#bar&&baz
            // will be interpreted as ((foo#bar)&&(baz))
            string("foo"),
            functionMatcher(),
            string("bar"),
            and(),
            string("baz")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                and(),
                n(
                    functionMatcher(),
                    n(string("foo")),
                    n(string("bar"))
                ),
                n(
                    string("baz")
                )
            ),
            root
        );
    }

    @Test
    void throwExceptionOnNestedMethodSeparator() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // classX#funX||classY#funY
            // will be interpreted as (classX#funX||classY)#funY
            string("classX"),
            functionMatcher(),
            string("funX"),
            or(),
            string("classY"),
            functionMatcher(),
            string("funY")
        ));
        try{
            Parser.parse(tokens);
            fail("Should throw exception for nested method matcher");
        } catch (CompilationException exc) {
            assertEquals("Nested method matcher (##).\n" +
                    "You have a function matcher inside your function matcher.\n" +
                    "That happens for example in classA#funA||classB#funB.\n" +
                    "Here, the function matcher is (funA||classB#funB).\n" +
                    "It should be declared as (classA#funA)||(classB#funB).\n" +
                    "Try using parenthesis around (class#function).", exc.getMessage());
        }
    }

    @Test
    void simpleParseWithTwoValuesWithOneFunctionMatcher() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // Only way to have complex function name matcher after hash is using parenthesis
            // foo#(bar||funBar)&&baz
            string("foo"),
            functionMatcher(),
            openParen(),
            string("bar"),
            or(),
            string("funBar"),
            closeParen(),
            and(),
            string("baz")
        ));
        ASTNode root = Parser.parse(tokens);
        assertEquals(
            n(
                and(),
                n(
                    functionMatcher(),
                    n(string("foo")),
                    n(
                        or(),
                        n(string("bar")),
                        n(string("funBar"))
                    )
                ),
                n(string("baz"))
            ),
            root
        );
    }


    @Test
    void simpleParseAnd() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // foo&&bar
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
    void simpleParseOr() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // foo||bar
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
    void simpleParseNot() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // !foo
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
    void simpleParseManyOperators() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            //foo&&bar||baz
            string("foo"),
            and(),
            string("bar"),
            or(),
            string("baz")
        )); // false && true || false = false because left comparison is first = ((false && true) || false)
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
    void simpleParseParenthesis() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // foo&&(bar||baz)
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
    void doubleParenthesis() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // ((foo&&bar)||baz)
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
    void singleItemParenthesis() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // (foo&&(bar)||baz)
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
    void tripleParenthesis() throws CompilationException {
        Deque<Token> tokens = new ArrayDeque<>(asList(
            // ((foo&&(bar)||(baz)))
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