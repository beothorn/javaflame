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
        MethodInstrumentationAgent.currentLevel = MethodInstrumentationAgent.LogLevel.DEBUG;
        FunctionCallRecorder.stackPerThread.clear();
        FunctionCallRecorder.shouldPrintQualified = false;
        FunctionCallRecorder.isRecording = true;
        FunctionCallRecorder.startTrigger = null;
        FunctionCallRecorder.stopTrigger = null;
    }

    @Test
    void happyDay() throws JSONException {
        onEnter("main", "a",  0); // main: enter a
            onEnter("main", "aa", 0); // main: a -> enter aa
            onLeave("main", 1); // main: leave aa -> a
            onEnter("main", "ab", 1); // main: a -> enter ab
        onEnter("t", "a", 0); // t: enter a
            onEnter("t", "b", 0); // t: a -> enter b
            onLeave("t", 1); // t: leave b -> a
            onLeave("main", 2); // main: leave ab -> a
            onEnter("main", "ac", 2); // main: a -> enter ac
                onEnter("main", "aca", 2); // main: a -> ac -> enter aca
                onLeave("main", 3); // main: leave aca -> ac
        onLeave("t", 1);  // t: leave a -> no root
            onLeave("main", 3); // main: leave ac -> a
        onLeave("main", 3); // main: leave a -> no root

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
    void happyDayWithTrigger() throws JSONException {
        FunctionCallRecorder.startTrigger = "startRecording";
        FunctionCallRecorder.stopTrigger = "stopRecording";
        FunctionCallRecorder.isRecording = false;

        onEnter("main", "ShouldNotRecordThisCall",  0);
        onEnter("other", "ShouldNotRecordThisCallOnOtherThread",  0);
            onEnter("main", "startRecording",  0);
                onEnter("main", "Fun",  0);
                    onEnter("main", "FunFun",  0);
                    onLeave("main", 0);
                onLeave("main", 0);
            onLeave("main", 0);
            onEnter("other", "OtherFun",  0);
                onEnter("other", "OtherFunFun",  0);
                onLeave("other", 0);
            onLeave("other", 0);
            onEnter("other", "stopRecording",  0);
            onEnter("main", "ShouldNotRecordThisCall",  0);
            onLeave("main", 0);
            onEnter("other", "ShouldNotRecordThisCall",  0);
            onLeave("other", 0);


        JSONObject otherThread = thread("other",0,
            // exit time 0 when leaving is unknown and it goes above the synthetic root
            // I am still not sure what is best here, to trap execution on synthetic root
            // or just let i leave the stack null.
            // It leaves the stack null for now.
            span("otherRoot",0,0,0,
                span("OtherFun",0,0,0,
                    span("OtherFunFun",0,0,0)
                )
            )
        );

        JSONObject threadMain = thread("main",0,
            span("mainRoot",0,0,0,
                span("startRecording",0,0,0,
                    span("Fun",0,0,0,
                        span("FunFun",0,0,0)
                    )
                )
            )
        );

        JSONArray expected = new JSONArray().put(otherThread).put(threadMain);
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
            throw new RuntimeException(jsonStringResult, e);
        }
    }
}