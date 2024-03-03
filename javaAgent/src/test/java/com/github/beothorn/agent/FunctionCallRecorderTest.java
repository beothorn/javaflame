package com.github.beothorn.agent;

import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.github.beothorn.agent.recorder.FunctionCallRecorder.onEnter;
import static com.github.beothorn.agent.recorder.FunctionCallRecorder.onLeave;
import static com.github.beothorn.agent.TestHelper.spanJSON;
import static com.github.beothorn.agent.TestHelper.threadJSON;
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
        e("main", "a", "a",  0); // main: enter a
            e("main", "aa", "aa", 0); // main: a -> enter aa
            onLeave("main", 1); // main: leave aa -> a
            e("main", "ab", "ab", 1); // main: a -> enter ab
        e("t", "a", "a", 0); // t: enter a
            e("t", "b", "b", 0); // t: a -> enter b
            onLeave("t", 1); // t: leave b -> a
            onLeave("main", 2); // main: leave ab -> a
            e("main", "ac", "ac", 2); // main: a -> enter ac
                e("main", "aca", "aca", 2); // main: a -> ac -> enter aca
                onLeave("main", 3); // main: leave aca -> ac
        onLeave("t", 1);  // t: leave a -> no root
            onLeave("main", 3); // main: leave ac -> a
        onLeave("main", 3); // main: leave a -> no root

        JSONObject threadT = threadJSON("t",0,
            spanJSON("tRoot", "tRoot", "tRoot", 0, -1, 0,
                spanJSON("a", "aClass", "a", 0, 1, 1,
                    spanJSON("b", "bClass", "b", 0,1,1)
                )
            )
        );

        JSONObject threadMain = threadJSON("main",0,
            spanJSON("mainRoot", "mainRoot", "mainRoot", 0, -1, 0,
                spanJSON("a", "aClass", "a", 0, 3, 3,
                    spanJSON("aa", "aaClass", "aa", 0, 1, 1),
                    spanJSON("ab", "abClass", "ab", 1, 2, 1),
                    spanJSON("ac", "acClass","ac", 2 , 3, 1,
                        spanJSON("aca", "acaClass", "aca", 2, 3, 1)
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

        e("main", "ShouldNotRecordThisCall", "",  0);
        e("other", "ShouldNotRecordThisCallOnOtherThread", "",  0);
        e("main", "startRecording", "startRecording",  0);
                e("main", "Fun", "Fun",  0);
                    e("main", "FunFun", "FunFun",  0);
                    onLeave("main", 0);
                onLeave("main", 0);
            onLeave("main", 0);
            e("other", "OtherFun", "OtherFun",  0);
                e("other", "OtherFunFun", "OtherFunFun",  0);
                onLeave("other", 0);
            onLeave("other", 0);
            e("other", "stopRecording", "stopRecording",  0);
            e("main", "ShouldNotRecordThisCall", "",  0);
            onLeave("main", 0);
            e("other", "ShouldNotRecordThisCall", "",  0);
            onLeave("other", 0);


        JSONObject otherThread = threadJSON("other",0,
            // exit time 0 when leaving is unknown and it goes above the synthetic root
            // I am still not sure what is best here, to trap execution on synthetic root
            // or just let i leave the stack null.
            // It leaves the stack null for now.
            spanJSON("otherRoot", "otherRoot", "otherRoot", 0, 0, 0,
                spanJSON("OtherFun", "OtherFunClass", "OtherFun", 0, 0, 0,
                    spanJSON("OtherFunFun", "OtherFunFunClass", "OtherFunFun", 0, 0, 0)
                )
            )
        );

        JSONObject threadMain = threadJSON("main",0,
            spanJSON("mainRoot", "mainRoot", "mainRoot", 0, 0, 0,
                spanJSON("startRecording", "startRecordingClass", "startRecording", 0, 0, 0,
                    spanJSON("Fun", "FunClass", "Fun", 0, 0, 0,
                        spanJSON("FunFun", "FunFunClass", "FunFun", 0, 0, 0)
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
        e("someThread", "A", "A",  0);
        onLeave("someThread", 1);
        e("someThread", "B", "B",  2);

        JSONObject someThread = threadJSON("someThread",0,
            spanJSON("someThreadRoot", "someThreadRoot", "someThreadRoot", 0, -1, 0,
                spanJSON("A", "AClass", "A", 0, 1, 1)
            )
        );

        JSONArray expected = new JSONArray().put(someThread);
        JSONArray actual = getOldStack();
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void shouldNotPrintEmptyCallStack(){
        assertTrue(FunctionCallRecorder.stackPerThread.isEmpty());
        assertTrue(!FunctionCallRecorder.getOldCallStack().isPresent());
        assertTrue(!FunctionCallRecorder.getFinalCallStack().isPresent());
    }

    private static void e(
        final String threadName,
        final String name,
        final String method,
        final long entryTime
    ){
        onEnter(
            threadName,
            name,
            name+"Class",
            method,
            entryTime
        );
    }

    private static JSONArray getOldStack() {
        String jsonStringResult = FunctionCallRecorder.getOldCallStack()
            .orElseThrow(RuntimeException::new)
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
            .orElseThrow(RuntimeException::new)
            .replaceAll("\n", "")
            .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,");
        try {
            return new JSONArray(jsonStringResult);
        } catch (JSONException e) {
            throw new RuntimeException(jsonStringResult, e);
        }
    }
}