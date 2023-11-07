package com.github.beothorn.agent;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Optional;

import static com.github.beothorn.agent.Span.span;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

class SpanTest {

    @Test
    void happyDayJson() throws JSONException {
        String[][] arguments = {
            {"int", "1"},
            {"String", "aaa"}
        };

        Span subject = span("foo", 0, arguments, 2, of(
            span("fooA", 0, 1),
            span("fooB", 1, 2)
        ));

        JSONObject expected = TestHelper.span("foo",0,2,2, arguments,
            TestHelper.span("fooA",0,1,1),
            TestHelper.span("fooB",1,2,1)
        );

        JSONObject actual = new JSONObject(subject.toJson());
        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    void jsonWithEscapedValues() throws JSONException {
        String name = "\" \n \\ \t \\\"";
        Span subject = span(name, 0, 2);
        JSONObject expected = TestHelper.span(name,0,2,2);
        JSONObject actual = new JSONObject(subject.toJson());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void jsonWithNullValues() throws JSONException {
        String name = "funAcceptsNull";
        Span subject = span(name, 0, new String[][]{
                {
                    "Object", null
                }
        });
        subject.returnValue = new String[]{
            "Object", null
        };
        JSONObject expected = TestHelper.span(name + " => Object null",0,-1,0,new String[][]{
                {
                    "Object", null
                }
        });
        expected.put("return", TestHelper.argument(new String[]{"Object", "null"}));
        JSONObject actual = new JSONObject(subject.toJson());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void jsonWithJsonArg() throws JSONException {
        // Sometimes a json is passed as a string to a function.
        // We should be able to render it on data.js
        String name = "{\n\t\"id\": \"123\"}";
        Span subject = span(name, 0, 2);
        JSONObject expected = TestHelper.span(name,0,2,2);
        JSONObject actual = new JSONObject(subject.toJson());
        JSONAssert.assertEquals(expected, actual, false);
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