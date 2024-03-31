package com.github.beothorn.agent;

import com.github.beothorn.agent.recorder.Span;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Optional;

import static com.github.beothorn.agent.recorder.Span.span;
import static com.github.beothorn.agent.TestHelper.of;
import static org.junit.jupiter.api.Assertions.*;

class SpanTest {

    @Test
    void happyDayJson() throws JSONException {
        String[][] arguments = {
            {"int", "1"},
            {"String", "aaa"}
        };

        Span subject = TestHelper.spanTest("1", "x.foo", "x","foo", 0, arguments, 2, of(
            TestHelper.spanTest("2", "x.fooA", "x", "fooA", 0, 1),
            TestHelper.spanTest("3", "x.fooB", "x", "fooB", 1, 2)
        ));

        JSONObject expected = TestHelper.spanJSON("x.foo", "x", "foo", 0,2,2, arguments,
            TestHelper.spanJSON("x.fooA", "x", "fooA", 0, 1, 1).put("id","2"),
            TestHelper.spanJSON("x.fooB", "x", "fooB", 1, 2, 1).put("id","3")
        ).put("id","1");

        String json = subject.toJson();
        JSONObject actual = new JSONObject(json);
        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    void jsonWithEscapedValues() throws JSONException {
        String name = "\" \n \\ \t \\\"";
        Span subject = TestHelper.spanTest(name, "method", 0, 2);
        JSONObject expected = TestHelper.spanJSON(name, "Class", "method", 0, 2, 2);
        JSONObject actual = new JSONObject(subject.toJson());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void jsonWithNullValues() throws JSONException {
        String name = "x.funAcceptsNull";
        String method = "funAcceptsNull";
        Span subject = span(name,"Class."+name, method, 0, new String[][]{
            {
                "Object", null
            }
        });
        subject.returnValue = new String[]{
            "Object", null
        };
        JSONObject expected = TestHelper.spanJSON(name + " => Object null", "Class."+name, method,0,-1,0,new String[][]{
            {
                "Object", null
            }
        });
        expected.put("return", TestHelper.argumentJSON(new String[]{"Object", "null"}));
        JSONObject actual = new JSONObject(subject.toJson());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void jsonWithJsonArg() throws JSONException {
        // Sometimes a json is passed as a string to a function.
        // We should be able to render it on data.js
        String name = "{\n\t\"id\": \"123\"}";
        Span subject = TestHelper.spanTest(name, "method", 0, 2);
        JSONObject expected = TestHelper.spanJSON(name, "Class", "method", 0, 2, 2);
        JSONObject actual = new JSONObject(subject.toJson());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void equalities(){
        Span subjectA = TestHelper.spanTest("root", "root", 0, 2, of(
            TestHelper.spanTest("x.A", "A", 0, 1),
            TestHelper.spanTest("x.B", "B", 1, 2)
        ));
        Span subjectAEquals = TestHelper.spanTest("root", "root", 0, 2, of(
            TestHelper.spanTest("x.A", "A", 0, 1),
            TestHelper.spanTest("x.B", "B", 1, 2)
        ));
        Span subjectANotEquals = TestHelper.spanTest("root", "root", 0, 3, of(
            TestHelper.spanTest("x.A", "A", 0, 1),
            TestHelper.spanTest("x.B", "B", 1, 2),
            TestHelper.spanTest("x.C", "C", 2, 3)
        ));
        Span subjectANotEqualsDeep = TestHelper.spanTest("root", "root", 0, 2, of(
            TestHelper.spanTest("x.A", "A", 0, 1),
            TestHelper.spanTest("x.B", "B", 1, 2, of(
                TestHelper.spanTest("x.C", "C", 1, 2)
            ))
        ));
        assertEquals(subjectA, subjectAEquals);
        assertNotEquals(subjectA, subjectANotEquals);
        assertNotEquals(subjectA, subjectANotEqualsDeep);
    }

    @Test
    void equalitiesDeep(){
        Span subjectA = TestHelper.spanTest("root", "root", 0, 2, of(
            TestHelper.spanTest("x.A", "A", 0, 1,  of(
                TestHelper.spanTest("x.AA", "AA", 0, 1, of(
                    TestHelper.spanTest("x.AAA", "AAA", 0, 1)
                ))
            )),
            TestHelper.spanTest("x.B", "B", 1, 2)
        ));
        Span subjectAEquals = TestHelper.spanTest("root", "root", 0, 2, of(
            TestHelper.spanTest("x.A", "A", 0, 1, of(
                TestHelper.spanTest("x.AA", "AA", 0, 1, of(
                    TestHelper.spanTest("x.AAA", "AAA", 0, 1)
                ))
            )),
            TestHelper.spanTest("x.B", "B", 1, 2)
        ));
        Span subjectANotEquals = TestHelper.spanTest("root", "root", 0, 2, of(
            TestHelper.spanTest("x.A", "A", 0, 1, of(
                TestHelper.spanTest("x.AA", "AA", 0, 1)
            )),
            TestHelper.spanTest("x.B", "B", 1, 2)
        ));
        Span subjectANotEqualsDeep = TestHelper.spanTest("root", "root", 0, 2, of(
            TestHelper.spanTest("x.A", "A", 0, 1, of(
                TestHelper.spanTest("x.AA", "AA", 0, 1, of(
                    TestHelper.spanTest("x.AAA", "AAA", 0, 1, of(
                        TestHelper.spanTest("x.AAAA", "AAAA", 0, 1)
                    ))
                ))
            )),
            TestHelper.spanTest("x.B", "B", 1, 2)
        ));
        assertEquals(subjectA, subjectAEquals);
        assertNotEquals(subjectA, subjectANotEquals);
        assertNotEquals(subjectA, subjectANotEqualsDeep);
    }

    @Test
    void shallowRemoveOldSpans(){
        Span subject = TestHelper.spanTest("root", "root", 0, of(
            TestHelper.spanTest("x.A", "A", 0, 1),
            TestHelper.spanTest("x.B", "B", 1, 2),
            TestHelper.spanTest("x.C", "C", 2, 3),
            TestHelper.spanTest("x.D", "D", 3)
        ));
        Span expectedOnlyOld = TestHelper.spanTest("root", "root", 0, of(
            TestHelper.spanTest("x.A", "A", 0, 1),
            TestHelper.spanTest("x.B", "B", 1, 2),
            TestHelper.spanTest("x.C", "C", 2, 3)
        ));
        Span expectedWithoutOld = TestHelper.spanTest("root", "root", 0, 4, of(
            TestHelper.spanTest("x.D", "D", 3, -1)
        ));
        Span actualOnlyOld = subject.removeFinishedFunction().orElseThrow(RuntimeException::new);
        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOnlyOld);
    }

    @Test
    void doNotRemoveUnfinished(){
        // If the function is not finished, it should not be removed.
        // Functions with exitTime == -1 are not finished.
        Span subject = TestHelper.spanTest("root", "root", 0, of(
            TestHelper.spanTest("x.A", "A", 0)
        ));
        Span expected = TestHelper.spanTest("root", "root", 0, of(
            TestHelper.spanTest("x.A", "A", 0)
        ));
        assertFalse(subject.removeFinishedFunction().isPresent());
        assertEquals(expected, subject);
    }

    @Test
    void addUnfinishedIfAFinishedIsIncluded(){
        // If the function is not finished, it should not be removed.
        // Functions with exitTime == -1 are not finished except when a child function is finished.
        Span subject = TestHelper.spanTest("root", "root", 0, of(
            TestHelper.spanTest("x.A", "A", 0, of(
                TestHelper.spanTest("x.AA", "AA", 0, 1),
                TestHelper.spanTest("x.AB", "AB", 0)
            ))
        ));
        Span expectedWithoutOld = TestHelper.spanTest("root", "root", 0, of(
            TestHelper.spanTest("x.A", "A", 0, of(
                TestHelper.spanTest("x.AB", "AB", 0)
            ))
        ));
        Span expectedOnlyOld = TestHelper.spanTest("root", "root", 0, of(
            TestHelper.spanTest("x.A", "A", 0, of(
                TestHelper.spanTest("x.AA", "AA", 0, 1)
            ))
        ));

        Span actualOnlyOld = subject.removeFinishedFunction().orElseThrow(RuntimeException::new);
        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOnlyOld);
    }

    @Test
    void happyDayRemoveOldSpans(){
        Span subject = TestHelper.spanTest("x.foo", "foo", 0, of(
            TestHelper.spanTest("x.fooA", "fooA", 0, 2, of(
                TestHelper.spanTest("x.fooAA", "fooAA", 0, 1),
                TestHelper.spanTest("x.fooAB", "fooAB", 1, 2)
            )),
            TestHelper.spanTest("x.fooB", "fooB", 2, of(
                TestHelper.spanTest("x.fooBA", "fooBA", 2, 3),
                TestHelper.spanTest("x.fooBB", "fooBB", 3)
            ))
        ));
        Span expectedOnlyOld = TestHelper.spanTest("x.foo", "foo", 0, of(
            TestHelper.spanTest("x.fooA", "fooA", 0, 2, of(
                TestHelper.spanTest("x.fooAA", "fooAA", 0, 1),
                TestHelper.spanTest("x.fooAB", "fooAB", 1, 2)
            )),
            TestHelper.spanTest("x.fooB", "fooB", 2, of(
                TestHelper.spanTest("x.fooBA", "fooBA", 2, 3)
            ))
        ));
        Span expectedWithoutOld = TestHelper.spanTest("x.foo", "foo", 0, of(
            TestHelper.spanTest("x.fooB", "fooB", 2, of(
                TestHelper.spanTest("x.fooBB", "fooBB", 3)
            ))
        ));

        Span actualOld = subject.removeFinishedFunction().orElseThrow(RuntimeException::new);

        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOld);
    }

    @Test
    void removeWithoutOldSpans(){
        Span subject = TestHelper.spanTest("x.foo", "foo", 0, of(
            TestHelper.spanTest("x.fooA", "fooA", 0, of(
                TestHelper.spanTest("x.fooAA", "fooAA", 0, of(
                    TestHelper.spanTest("x.fooAAA", "fooAAA", 0)
                ))
            ))
        ));
        Span expected = TestHelper.spanTest("x.foo", "foo", 0, of(
            TestHelper.spanTest("x.fooA", "fooA", 0, of(
                TestHelper.spanTest("x.fooAA", "fooAA", 0, of(
                    TestHelper.spanTest("x.fooAAA", "fooAAA", 0)
                ))
            ))
        ));

        Optional<Span> actualOld = subject.removeFinishedFunction();

        assertFalse(actualOld.isPresent());
        assertEquals(expected, subject);
    }

    @Test
    void spanFlow(){
        Span subject = TestHelper.spanTest("root", "root", 0)
                .enter("x.A", "x", "A", 0)
                    .enter("x.AA", "x", "AA", 0)
                        .leave(1)
                    .enter("x.AB", "x", "AB", 1)
                        .leave(2)
                    .leave(2)
                .enter("x.B", "x", "B", 2)
                    .enter("x.BA", "x", "BA", 2)
                        .leave(3)
                    .enter("x.BB", "x", "BB", 3)
                        .leave(4);

        Span expected = TestHelper.spanTest("root", "root", 0, of(
            TestHelper.spanTest("x.A", "A", 0, 2, of(
                TestHelper.spanTest("x.AA", "AA", 0, 1),
                TestHelper.spanTest("x.AB", "AB", 1, 2)
            )),
            TestHelper.spanTest("x.B", "B", 2, 4, of(
                TestHelper.spanTest("x.BA", "BA", 2, 3),
                TestHelper.spanTest("x.BB", "BB", 3, 4)
            ))
        ));

        Span root = subject.getRoot();
        assertEquals(expected, root);
    }

    @Test
    void removeFinishedFunctionWithNoChildren(){
        Span subject = TestHelper.spanTest("x.foo", "foo", 0, of(
            TestHelper.spanTest("x.fooA", "fooA", 0, 2, of(
                TestHelper.spanTest("x.fooAA", "fooAA", 0, 1)
            ))
        ));
        Span expectedOnlyOld = TestHelper.spanTest("x.foo", "foo", 0, of(
            TestHelper.spanTest("x.fooA", "fooA", 0, 2, of(
                TestHelper.spanTest("x.fooAA", "fooAA", 0, 1)
            ))
        ));
        Span expectedWithoutOld = TestHelper.spanTest("x.foo", "foo", 0, 2);

        Span actualOld = subject.removeFinishedFunction().orElseThrow(RuntimeException::new);

        assertEquals(expectedWithoutOld, subject);
        assertEquals(expectedOnlyOld, actualOld);
    }

}