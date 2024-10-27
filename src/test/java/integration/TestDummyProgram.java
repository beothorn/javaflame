package integration;

import com.github.beothorn.agent.TestHelper;
import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.github.beothorn.agent.TestHelper.threadJSON;
import static com.github.beothorn.agent.recorder.FunctionCallRecorderWithValueCapturing.enterFunction;
import static com.github.beothorn.agent.recorder.FunctionCallRecorderWithValueCapturing.exit;

public class TestDummyProgram {

    public static void main(String[] args) {
        new TestDummyProgram().run("{\"bar\":\n\"baz\"}");
        System.out.println(FunctionCallRecorder.getFinalCallStack());
    }

    public void run(String foo) {
        try {
            enterFunction(
                    TestDummyProgram.class.getDeclaredMethod("run", String.class),
                    new Object[]{foo}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        a(1);
        b(1, 2);

        exit(null);
    }

    private int a(int p1){
        try {
            enterFunction(
                TestDummyProgram.class.getDeclaredMethod("a", int.class),
                new Object[]{p1}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int result = aa(p1 + 1);

        exit(result);
        return result;
    }

    private int aa(int p1){
        try {
            enterFunction(
                TestDummyProgram.class.getDeclaredMethod("aa", int.class),
                new Object[]{p1}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int result = p1 + 1;
        exit(result);
        return result;
    }

    private int b(int p1, int p2){
        try {
            enterFunction(
                TestDummyProgram.class.getDeclaredMethod(
                    "b", int.class, int.class
                ),
                new Object[]{p1, p2}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int result = bb(p1 + 1, p2 + 1);

        exit(result);
        return result;
    }

    private int bb(int p1, int p2){
        try {
            enterFunction(
                TestDummyProgram.class.getDeclaredMethod(
                    "bb", int.class, int.class
                ),
                new Object[]{p1, p2}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int result = p1 + p2;

        exit(result);
        return result;
    }

    @Test
    void runDummyProgramAndCheckOutput() throws JSONException, InterruptedException {
        FunctionCallRecorder.stackPerThread.clear();
        FunctionCallRecorder.shouldPrintQualified = true;
        Thread runner = new Thread(() -> new TestDummyProgram().run("{\"bar\":\n\"baz\"}"));
        runner.setName("mainFAIL"); // When running with gradle, test is wrapped in a different thread so we make sure the runner thread is always called main
        runner.start();
        runner.join();

        JSONArray expected = new JSONArray().put(
            threadJSON("main", 0,
                TestHelper.spanJSON("mainRoot", "mainRoot", "mainRoot",0,-1,0,
                    TestHelper.spanJSON(
                        "integration.TestDummyProgram.run(java.lang.String = {\"bar\":\n\"baz\"}) => void",
                        "integration.TestDummyProgram",
                        "run",
                        0,
                        0,
                        0,
                        TestHelper.spanJSON(
                            "integration.TestDummyProgram.a(int = 1) => java.lang.Integer 3",
                            "integration.TestDummyProgram",
                            "a",
                            0,
                            0,
                            0,
                            TestHelper.spanJSON(
                                "integration.TestDummyProgram.aa(int = 2) => java.lang.Integer 3",
                                "integration.TestDummyProgram",
                                "aa",
                                0,
                                0,
                                0
                            )
                        ),
                        TestHelper.spanJSON(
                            "integration.TestDummyProgram.b(int = 1, int = 2) => java.lang.Integer 5",
                            "integration.TestDummyProgram",
                            "b",
                            0,
                            0
                            ,0,
                            TestHelper.spanJSON(
                                "integration.TestDummyProgram.bb(int = 2, int = 3) => java.lang.Integer 5",
                                "integration.TestDummyProgram",
                                "bb",
                                0,
                                0,
                                0
                            )
                        )
                    )
                )
            )
        );
        String finalStack = getFinalStack();
        JSONArray actual = new JSONArray(finalStack);
        JSONAssert.assertEquals(expected, actual, false);
    }

    private static String getFinalStack() {
        return FunctionCallRecorder.getFinalCallStack()
            .orElseThrow(RuntimeException::new)
            .replaceAll("\n", "")
            .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,")
            .replaceAll("\"entryTime\":[0-9]+,", "\"entryTime\":0,")
            .replaceAll("\"exitTime\":[0-9]+,", "\"exitTime\":0,")
            .replaceAll("\"value\":[0-9]+", "\"value\":0");
    }

}
