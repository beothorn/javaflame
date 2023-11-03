package com.github.beothorn.agent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.github.beothorn.agent.FunctionCallRecorder.onEnter;
import static com.github.beothorn.agent.FunctionCallRecorder.onLeave;
import static com.github.beothorn.agent.TestHelper.span;
import static com.github.beothorn.agent.TestHelper.thread;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FunctionCallRecorderTest {

    @BeforeEach
    void setUp() {
        FunctionCallRecorder.stackPerThread.clear();
    }

    @Test
    void happyDay() throws JSONException {
        onEnter("main", "a",  0); // main: a
        onEnter("main", "aa", 0); // main: a -> aa
        onLeave("main", 1); // main: a
        onEnter("main", "ab", 1); // main: a -> ab
        onEnter("t", "a", 0); // t: a
        onEnter("t", "b", 0); // t: a -> b
        onLeave("t", 1); // t: a
        onLeave("main", 2); // main: a
        onEnter("main", "ac", 2); // main: a -> ac
        onEnter("main", "aca", 2); // main: a -> ac -> aca
        onLeave("main", 3); // main: a -> ac
        onLeave("t", 1);  // t: no root
        onLeave("main", 3); // main: a
        onLeave("main", 3); // main: exit a

        JSONObject threadT = thread("t",0,
            span("tRoot",0,-1,0,
                span("a",0,1,1,
                    span("b",0,1,1)
                )
            )
        );

        JSONObject threadMain = thread("main",0,
            span("mainRoot",0,-1,0,
                span("a",0,3,3,
                    span("aa",0,1,1),
                    span("ab",1,2,1),
                    span("ac",2,3,1,
                        span("aca",2,3,1)
                    )
                )
            )
        );

        JSONArray expected = new JSONArray().put(threadT).put(threadMain);
        JSONArray actual = getFinalStack();
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void getOldCallStackHappyDay() throws JSONException {
        onEnter("someThread", "A",  0);
        onLeave("someThread", 1);
        onEnter("someThread", "B",  2);

        JSONObject someThread = thread("someThread",0,
            span("someThreadRoot",0,-1,0,
                span("A",0,1,1)
            )
        );

        JSONArray expected = new JSONArray().put(someThread);
        JSONArray actual = getOldStack();
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void shouldNotPrintEmptyCallStack(){
        assertTrue(FunctionCallRecorder.stackPerThread.isEmpty());
        assertTrue(FunctionCallRecorder.getOldCallStack().isEmpty());
        assertTrue(FunctionCallRecorder.getFinalCallStack().isEmpty());
    }

    private static JSONArray getOldStack() {
        String jsonStringResult = FunctionCallRecorder.getOldCallStack()
            .orElseThrow()
            .replaceAll("\n", "")
            .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,");
        try {
            return new JSONArray(jsonStringResult);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONArray getFinalStack() {
        String jsonStringResult = FunctionCallRecorder.getFinalCallStack()
            .orElseThrow()
            .replaceAll("\n", "")
            .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,");
        try {
            return new JSONArray(jsonStringResult);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}