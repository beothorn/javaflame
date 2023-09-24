package integration;

import com.github.beothorn.agent.SpanCatcher;
import org.junit.jupiter.api.Test;

import static com.github.beothorn.agent.SpanCatcherDetailed.enter;
import static com.github.beothorn.agent.SpanCatcherDetailed.exit;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyProgram {

    public static void main(String[] args) {
        new DummyProgram().run();
        System.out.println(SpanCatcher.getFinalCallStack());
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

        exit(-1);
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

        exit(-1);
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

        exit(-1);
        return p1 + 1;
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

        exit(-1);
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

        exit(-1);
        return result;
    }

    @Test
    void runDummyProgramAndCheckOutput(){
        SpanCatcher.stackPerThread.clear();
        new DummyProgram().run();
        assertEquals("[" +
            "{" +
                "\"thread\":\"main\"," +
                "\"span\":{" +
                    "\"name\":\"integration.DummyProgram.run()\"," +
                    "\"value\":0," +
                    "\"children\":[" +
                        "{" +
                            "\"name\":\"integration.DummyProgram.a(int arg0 = 1)\"," +
                            "\"value\":0," +
                            "\"children\":[" +
                                "{" +
                                    "\"name\":\"integration.DummyProgram.aa(int arg0 = 2)\"," +
                                    "\"value\":0" +
                                "}" +
                            "]" +
                        "}," +
                        "{" +
                            "\"name\":\"integration.DummyProgram.b(int arg0 = 1, int arg1 = 2)\"," +
                            "\"value\":0," +
                            "\"children\":[" +
                                "{" +
                                    "\"name\":\"integration.DummyProgram.bb(int arg0 = 2, int arg1 = 3)\"," +
                                    "\"value\":0" +
                                "}" +
                            "]" +
                        "}" +
                    "]" +
                "}" +
            "}" +
        "]", SpanCatcher.getFinalCallStack());
    }

}
