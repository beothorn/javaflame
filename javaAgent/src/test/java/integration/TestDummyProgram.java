package integration;

import com.github.beothorn.agent.FunctionCallRecorder;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.github.beothorn.agent.FunctionCallRecorderWithValueCapturing.enter;
import static com.github.beothorn.agent.FunctionCallRecorderWithValueCapturing.exit;
import static com.github.beothorn.agent.TestHelper.span;
import static com.github.beothorn.agent.TestHelper.thread;

public class TestDummyProgram {

    public static void main(String[] args) {
        new TestDummyProgram().run();
        System.out.println(FunctionCallRecorder.getFinalCallStack());
    }

    public void run() {
        try {
            enter(
                    TestDummyProgram.class.getDeclaredMethod("run"),
                    new Object[]{}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        a(1);
        b(1, 2);

        exit(-1, null);
    }

    private int a(int p1){
        try {
            enter(
                TestDummyProgram.class.getDeclaredMethod("a", int.class),
                new Object[]{p1}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int result = aa(p1 + 1);

        exit(-1, result);
        return result;
    }

    private int aa(int p1){
        try {
            enter(
                TestDummyProgram.class.getDeclaredMethod("aa", int.class),
                new Object[]{p1}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int result = p1 + 1;
        exit(-1, result);
        return result;
    }

    private int b(int p1, int p2){
        try {
            enter(
                TestDummyProgram.class.getDeclaredMethod(
                    "b", int.class, int.class
                ),
                new Object[]{p1, p2}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int result = bb(p1 + 1, p2 + 1);

        exit(-1, result);
        return result;
    }

    private int bb(int p1, int p2){
        try {
            enter(
                TestDummyProgram.class.getDeclaredMethod(
                    "bb", int.class, int.class
                ),
                new Object[]{p1, p2}
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int result = p1 + p2;

        exit(-1, result);
        return result;
    }

    @Test
    void runDummyProgramAndCheckOutput() throws JSONException {
        FunctionCallRecorder.stackPerThread.clear();
        FunctionCallRecorder.shouldPrintQualified = true;
        new TestDummyProgram().run();

        JSONArray expected = new JSONArray().put(
            thread("main", 0,
                span("mainRoot",0,-1,0,
                    span("integration.TestDummyProgram.run() => null",0,0,0,
                        span("integration.TestDummyProgram.a(int = 1) => 3",0,0,0,
                            span("integration.TestDummyProgram.aa(int = 2) => 3",0,0,0)
                        ),
                        span("integration.TestDummyProgram.b(int = 1, int = 2) => 5",0,0,0,
                            span("integration.TestDummyProgram.bb(int = 2, int = 3) => 5",0,0,0)
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
            .orElseThrow()
            .replaceAll("\n", "")
            .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,")
            .replaceAll("\"entryTime\":[0-9]+,", "\"entryTime\":0,")
            .replaceAll("\"exitTime\":[0-9]+,", "\"exitTime\":0,")
            .replaceAll("\"value\":[0-9]+", "\"value\":0");
    }

}
