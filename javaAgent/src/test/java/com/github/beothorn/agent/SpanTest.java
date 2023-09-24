package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.beothorn.agent.Span.span;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

class SpanTest {

    @Test
    void happyDayJson(){
        Span subject = span("foo", 10, of(
            span("fooA", 5, of()),
            span("fooB", 5, of())
        ));
        String expected = "{" +
            "\"name\":\"foo\"," +
            "\"value\":10," +
            "\"children\":[" +
                "{" +
                    "\"name\":\"fooA\"," +
                    "\"value\":5" +
                "}," +
                "{" +
                    "\"name\":\"fooB\"," +
                    "\"value\":5" +
                "}" +
            "]" +
        "}";
        assertEquals(expected, subject.toJson());
    }

    @Test
    void equalities(){
        Span subjectA = span("root", 10, of(
            span("A", 5, of()),
            span("B", 5, of())
        ));
        Span subjectAEquals = span("root", 10, of(
            span("A", 5, of()),
            span("B", 5, of())
        ));
        Span subjectANotEquals = span("root", 10, of(
            span("A", 5, of()),
            span("B", 5, of()),
            span("C", 5, of())
        ));
        Span subjectANotEqualsDeep = span("root", 10, of(
            span("A", 5, of()),
            span("B", 5, of(
                span("C", 5, of())
            ))
        ));
        assertEquals(subjectA, subjectAEquals);
        assertNotEquals(subjectA, subjectANotEquals);
        assertNotEquals(subjectA, subjectANotEqualsDeep);
    }

    @Test
    void equalitiesDeep(){
        Span subjectA = span("root", 10, of(
            span("A", 5, of(
                span("AA", 5, of(
                    span("AAA", 5, of())
                ))
            )),
            span("B", 5, of())
        ));
        Span subjectAEquals = span("root", 10, of(
            span("A", 5, of(
                span("AA", 5, of(
                    span("AAA", 5, of())
                ))
            )),
            span("B", 5, of())
        ));
        Span subjectANotEquals = span("root", 10, of(
            span("A", 5, of(
                span("AA", 5, of())
            )),
            span("B", 5, of())
        ));
        Span subjectANotEqualsDeep = span("root", 10, of(
            span("A", 5, of(
                span("AA", 5, of(
                    span("AAA", 5, of(
                        span("AAAA", 5, of())
                    ))
                ))
            )),
            span("B", 5, of())
        ));
        assertEquals(subjectA, subjectAEquals);
        assertNotEquals(subjectA, subjectANotEquals);
        assertNotEquals(subjectA, subjectANotEqualsDeep);
    }


    @Test
    void shallowRemoveOldSpans(){
        Span subject = span("root", 10, of(
            span("A", 5, of()),
            span("B", 5, of()),
            span("C", 5, of()),
            span("D", 5, of())
        ));
        Span expectedOnlyOld = span("root", 10, of(
            span("A", 5, of()),
            span("B", 5, of()),
            span("C", 5, of())
        ));
        Span expectedWithoutOld = span("root", 10, of(
            span("D", 5, of())
        ));
        Span actualOnlyOld = subject.removePastSpans().orElseThrow();
        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOnlyOld);
    }

    @Test
    void happyDayRemoveOldSpans(){
        Span subject = span("foo", 10, of(
            span("fooA", 5, of(
                span("fooAA", 5, of()),
                span("fooAB", 5, of())
            )),
            span("fooB", 5, of(
                span("fooBA", 5, of()),
                span("fooBB", 5, of())
            ))
        ));
        Span expectedOnlyOld = span("foo", 10, of(
            span("fooA", 5, of(
                span("fooAA", 5, of()),
                span("fooAB", 5, of())
            )),
            span("fooB", 5, of(
                span("fooBA", 5, of())
            ))
        ));
        Span expectedWithoutOld = span("foo", 10, of(
            span("fooB", 5, of(
                span("fooBB", 5, of())
            ))
        ));

        Span actualOld = subject.removePastSpans().orElseThrow();

        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOld);
    }

    @Test
    void removeWithoutOldSpans(){
        Span subject = span("foo", 10, of(
            span("fooA", 5, of(
                span("fooAA", 5, of(
                    span("fooAAA", 5, of())
                ))
            ))
        ));
        Span expected = span("foo", 10, of(
            span("fooA", 5, of(
                span("fooAA", 5, of(
                    span("fooAAA", 5, of())
                ))
            ))
        ));

        Optional<Span> actualOld = subject.removePastSpans();

        assertTrue(actualOld.isEmpty());
        assertEquals(expected, subject);
    }

    @Test
    void spanFlow(){
        Span subject = span("root")
                .enter(span("A"))
                    .enter(span("AA"))
                        .leave()
                    .enter(span("AB"))
                        .leave()
                    .leave()
                .enter(span("B"))
                    .enter(span("BA"))
                        .leave()
                    .enter(span("BB"))
                        .leave();

        Span expected = span("root", 0, of(
            span("A", 0, of(
                span("AA", 0, of()),
                span("AB", 0, of())
            )),
            span("B", 0, of(
                span("BA", 0, of()),
                span("BB", 0, of())
            ))
        ));

        Span root = subject.getRoot();
        assertEquals(expected, root);
    }

}