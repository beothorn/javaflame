package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.beothorn.agent.Span.span;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

class SpanTest {

    @Test
    void happyDayJson(){
        Span subject = span("foo", 0, 2, of(
            span("fooA", 0, 1),
            span("fooB", 1, 2)
        ));
        String expected = "{" +
            "\"name\":\"foo\"," +
            "\"entryTime\":0," +
            "\"exitTime\":2," +
            "\"value\":2," +
            "\"children\":[" +
                "{" +
                    "\"name\":\"fooA\"," +
                    "\"entryTime\":0," +
                    "\"exitTime\":1," +
                    "\"value\":1" +
                "}," +
                "{" +
                    "\"name\":\"fooB\"," +
                    "\"entryTime\":1," +
                    "\"exitTime\":2," +
                    "\"value\":1" +
                "}" +
            "]" +
        "}";
        assertEquals(expected, subject.toJson()
                .replaceAll("\n", ""));
    }

    @Test
    void jsonWithEscapedValues(){
        Span subject = span("foo(String a=\"a\nb\tc\")", 0, 2);
        String expected = "{" +
                "\"name\":\"foo(String a=\\\"a\\nb\\tc\\\")\"," +
                "\"entryTime\":0," +
                "\"exitTime\":2," +
                "\"value\":2" +
                "}\n";
        assertEquals(expected, subject.toJson());
    }

    @Test
    void equalities(){
        Span subjectA = span("root", 0, 2, of(
            span("A", 0, 1),
            span("B", 1, 2)
        ));
        Span subjectAEquals = span("root", 0, 2, of(
            span("A", 0, 1),
            span("B", 1, 2)
        ));
        Span subjectANotEquals = span("root", 0, 3, of(
            span("A", 0, 1),
            span("B", 1, 2),
            span("C", 2, 3)
        ));
        Span subjectANotEqualsDeep = span("root", 0, 2, of(
            span("A", 0, 1),
            span("B", 1, 2, of(
                span("C", 1, 2)
            ))
        ));
        assertEquals(subjectA, subjectAEquals);
        assertNotEquals(subjectA, subjectANotEquals);
        assertNotEquals(subjectA, subjectANotEqualsDeep);
    }

    @Test
    void equalitiesDeep(){
        Span subjectA = span("root", 0, 2, of(
            span("A", 0, 1,  of(
                span("AA", 0, 1, of(
                    span("AAA", 0, 1)
                ))
            )),
            span("B", 1, 2)
        ));
        Span subjectAEquals = span("root", 0, 2, of(
            span("A", 0, 1, of(
                span("AA", 0, 1, of(
                    span("AAA", 0, 1)
                ))
            )),
            span("B", 1, 2)
        ));
        Span subjectANotEquals = span("root", 0, 2, of(
            span("A", 0, 1, of(
                span("AA", 0, 1)
            )),
            span("B", 1, 2)
        ));
        Span subjectANotEqualsDeep = span("root", 0, 2, of(
            span("A", 0, 1, of(
                span("AA", 0, 1, of(
                    span("AAA", 0, 1, of(
                        span("AAAA", 0, 1)
                    ))
                ))
            )),
            span("B", 1, 2)
        ));
        assertEquals(subjectA, subjectAEquals);
        assertNotEquals(subjectA, subjectANotEquals);
        assertNotEquals(subjectA, subjectANotEqualsDeep);
    }


    @Test
    void shallowRemoveOldSpans(){
        Span subject = span("root", 0, of(
            span("A", 0, 1),
            span("B", 1, 2),
            span("C", 2, 3),
            span("D", 3)
        ));
        Span expectedOnlyOld = span("root", 0, of(
            span("A", 0, 1),
            span("B", 1, 2),
            span("C", 2, 3)
        ));
        Span expectedWithoutOld = span("root", 0, 4, of(
            span("D", 3, -1)
        ));
        Span actualOnlyOld = subject.removeFinishedFunction().orElseThrow();
        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOnlyOld);
    }

    @Test
    void doNotRemoveUnfinished(){
        // If the function is not finished, it should not be removed.
        // Functions with exitTime == -1 are not finished.
        Span subject = span("root", 0, of(
            span("A", 0)
        ));
        Span expected = span("root", 0, of(
            span("A", 0)
        ));
        assertTrue(subject.removeFinishedFunction().isEmpty());
        assertEquals(expected, subject);
    }

    @Test
    void addUnfinishedIfAFinishedIsIncluded(){
        // If the function is not finished, it should not be removed.
        // Functions with exitTime == -1 are not finished except when a child function is finished.
        Span subject = span("root", 0, of(
            span("A", 0, of(
                span("AA", 0, 1),
                span("AB", 0)
            ))
        ));
        Span expectedWithoutOld = span("root", 0, of(
            span("A", 0, of(
                span("AB", 0)
            ))
        ));
        Span expectedOnlyOld = span("root", 0, of(
            span("A", 0, of(
                span("AA", 0, 1)
            ))
        ));

        Span actualOnlyOld = subject.removeFinishedFunction().orElseThrow();
        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOnlyOld);
    }

    @Test
    void happyDayRemoveOldSpans(){
        Span subject = span("foo", 0, of(
            span("fooA", 0, 2, of(
                span("fooAA", 0, 1),
                span("fooAB", 1, 2)
            )),
            span("fooB", 2, of(
                span("fooBA", 2, 3),
                span("fooBB", 3)
            ))
        ));
        Span expectedOnlyOld = span("foo", 0, of(
            span("fooA", 0, 2, of(
                span("fooAA", 0, 1),
                span("fooAB", 1, 2)
            )),
            span("fooB", 2, of(
                span("fooBA", 2, 3)
            ))
        ));
        Span expectedWithoutOld = span("foo", 0, of(
            span("fooB", 2, of(
                span("fooBB", 3)
            ))
        ));

        Span actualOld = subject.removeFinishedFunction().orElseThrow();

        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOld);
    }

    @Test
    void removeWithoutOldSpans(){
        Span subject = span("foo", 0, of(
            span("fooA", 0, of(
                span("fooAA", 0, of(
                    span("fooAAA", 0)
                ))
            ))
        ));
        Span expected = span("foo", 0, of(
            span("fooA", 0, of(
                span("fooAA", 0, of(
                    span("fooAAA", 0)
                ))
            ))
        ));

        Optional<Span> actualOld = subject.removeFinishedFunction();

        assertTrue(actualOld.isEmpty());
        assertEquals(expected, subject);
    }

    @Test
    void spanFlow(){
        Span subject = span("root", 0)
                .enter("A", 0)
                    .enter("AA", 0)
                        .leave(1)
                    .enter("AB", 1)
                        .leave(2)
                    .leave(2)
                .enter("B", 2)
                    .enter("BA", 2)
                        .leave(3)
                    .enter("BB", 3)
                        .leave(4);

        Span expected = span("root", 0, of(
            span("A", 0, 2, of(
                span("AA", 0, 1),
                span("AB", 1, 2)
            )),
            span("B", 2, 4, of(
                span("BA", 2, 3),
                span("BB", 3, 4)
            ))
        ));

        Span root = subject.getRoot();
        assertEquals(expected, root);
    }

    @Test
    void removeFinishedFunctionWithNoChildren(){
        Span subject = span("foo", 0, of(
            span("fooA", 0, 2, of(
                    span("fooAA", 0, 1)
            ))
        ));
        Span expectedOnlyOld = span("foo", 0, of(
                span("fooA", 0, 2, of(
                        span("fooAA", 0, 1)
                ))
        ));
        Span expectedWithoutOld = span("foo", 0, 2);

        Span actualOld = subject.removeFinishedFunction().orElseThrow();

        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOld);
    }

}