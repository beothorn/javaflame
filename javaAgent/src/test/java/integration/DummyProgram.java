package integration;

import com.github.beothorn.agent.FunctionCallRecorder;
import org.junit.jupiter.api.Test;

import static com.github.beothorn.agent.FunctionCallRecorderWithValueCapturing.enter;
import static com.github.beothorn.agent.FunctionCallRecorderWithValueCapturing.exit;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyProgram {

    public static void main(String[] args) {
        new DummyProgram().run();
        System.out.println(FunctionCallRecorder.getFinalCallStack());
    }

    public void run() {
        try {
            enter(
                    DummyProgram.class.getDeclaredMethod("run"),
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
                DummyProgram.class.getDeclaredMethod("a", int.class),
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
                DummyProgram.class.getDeclaredMethod("aa", int.class),
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
                DummyProgram.class.getDeclaredMethod(
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
                DummyProgram.class.getDeclaredMethod(
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
    void runDummyProgramAndCheckOutput(){
        FunctionCallRecorder.stackPerThread.clear();
        FunctionCallRecorder.shouldPrintQualified = true;
        new DummyProgram().run();
        assertEquals("[" +
            "{" +
                "\"thread\":\"main\"," +
                "\"snapshotTime\":0," +
                "\"span\":{" +
                    "\"name\":\"mainRoot\"," +
                    "\"entryTime\":0," +
                    "\"exitTime\":-1," +
                    "\"value\":0," +
                    "\"children\":[" +
                        "{" +
                            "\"name\":\"integration.DummyProgram.run() => null\"," +
                            "\"entryTime\":0," +
                            "\"exitTime\":0," +
                            "\"value\":0," +
                            "\"children\":[" +
                                "{" +
                                    "\"name\":\"integration.DummyProgram.a(int arg0 = 1) => 3\"," +
                                    "\"entryTime\":0," +
                                    "\"exitTime\":0," +
                                    "\"value\":0," +
                                    "\"children\":[" +
                                        "{" +
                                            "\"name\":\"integration.DummyProgram.aa(int arg0 = 2) => 3\"," +
                                            "\"entryTime\":0," +
                                            "\"exitTime\":0," +
                                            "\"value\":0" +
                                        "}" +
                                    "]" +
                                "}," +
                                "{" +
                                    "\"name\":\"integration.DummyProgram.b(int arg0 = 1, int arg1 = 2) => 5\"," +
                                    "\"entryTime\":0," +
                                    "\"exitTime\":0," +
                                    "\"value\":0," +
                                    "\"children\":[" +
                                        "{" +
                                            "\"name\":\"integration.DummyProgram.bb(int arg0 = 2, int arg1 = 3) => 5\"," +
                                            "\"entryTime\":0," +
                                            "\"exitTime\":0," +
                                            "\"value\":0" +
                                        "}" +
                                    "]" +
                                "}" +
                            "]" +
                        "}" +
                    "]" +
                "}" +
            "}" +
        "]", FunctionCallRecorder.getFinalCallStack()
                .orElseThrow()
                .replaceAll("\n", "")
                .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,")
                .replaceAll("\"entryTime\":[0-9]+,", "\"entryTime\":0,")
                .replaceAll("\"exitTime\":[0-9]+,", "\"exitTime\":0,")
                .replaceAll("\"value\":[0-9]+", "\"value\":0")
        );
    }

}
