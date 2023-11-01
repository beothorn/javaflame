package com.github.beothorn.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.beothorn.agent.FunctionCallRecorder.onEnter;
import static com.github.beothorn.agent.FunctionCallRecorder.onLeave;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FunctionCallRecorderTest {

    @BeforeEach
    void setUp() {
        FunctionCallRecorder.stackPerThread.clear();
    }

    @Test
    void happyDay(){
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

        String threadT = "{" +
            "\"thread\":\"t\"," +
            "\"snapshotTime\":0,"+
            "\"span\":{" +
                "\"name\":\"tRoot\"," +
                "\"entryTime\":0," +
                "\"exitTime\":-1," +
                "\"value\":0," +
                "\"children\":[" +
                    "{" +
                        "\"name\":\"a\"," +
                        "\"entryTime\":0," +
                        "\"exitTime\":1," +
                        "\"value\":1," +
                        "\"children\":[" +
                            "{" +
                                "\"name\":\"b\"," +
                                "\"entryTime\":0," +
                                "\"exitTime\":1," +
                                "\"value\":1" +
                            "}" +
                        "]" +
                    "}" +
                "]" +
            "}" +
        "}";
        String threadMain = "{" +
            "\"thread\":\"main\"," +
            "\"snapshotTime\":0,"+
            "\"span\":{" +
                "\"name\":\"mainRoot\"," +
                "\"entryTime\":0," +
                "\"exitTime\":-1," +
                "\"value\":0," +
                "\"children\":[" +
                    "{" +
                        "\"name\":\"a\"," +
                        "\"entryTime\":0," +
                        "\"exitTime\":3," +
                        "\"value\":3," +
                        "\"children\":[" +
                            "{" +
                                "\"name\":\"aa\"," +
                                "\"entryTime\":0," +
                                "\"exitTime\":1," +
                                "\"value\":1" +
                            "}," +
                            "{" +
                                "\"name\":\"ab\"," +
                                "\"entryTime\":1," +
                                "\"exitTime\":2," +
                                "\"value\":1" +
                            "}," +
                            "{" +
                                "\"name\":\"ac\"," +
                                "\"entryTime\":2," +
                                "\"exitTime\":3," +
                                "\"value\":1," +
                                "\"children\":[" +
                                    "{" +
                                        "\"name\":\"aca\"," +
                                        "\"entryTime\":2," +
                                        "\"exitTime\":3," +
                                        "\"value\":1" +
                                    "}" +
                                "]" +
                            "}" +
                        "]" +
                    "}" +
                "]" +
            "}" +
        "}";
        assertEquals(
            "[" + threadT + "," + threadMain +"]",
            FunctionCallRecorder.getFinalCallStack()
                    .orElseThrow()
                    .replaceAll("\n", "")
                    .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,")
        );
    }

    @Test
    void getOldCallStackHappyDay(){
        onEnter("someThread", "A",  0);
        onLeave("someThread", 1);
        onEnter("someThread", "B",  2);
        assertEquals("[" +
            "{" +
                "\"thread\":\"someThread\"," +
                "\"snapshotTime\":0," +
                "\"span\":{" +
                    "\"name\":\"someThreadRoot\"," +
                    "\"entryTime\":0," +
                    "\"exitTime\":-1," +
                    "\"value\":0," +
                    "\"children\":[" +
                        "{" +
                            "\"name\":\"A\"," +
                            "\"entryTime\":0," +
                            "\"exitTime\":1," +
                            "\"value\":1" +
                        "}" +
                    "]" +
                "}" +
            "}" +
        "]",
        FunctionCallRecorder.getOldCallStack()
                .orElseThrow()
                .replaceAll("\n", "")
                .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,")
        );
    }

    @Test
    void shouldNotPrintEmptyCallStack(){
        assertTrue(FunctionCallRecorder.stackPerThread.isEmpty());
        assertTrue(FunctionCallRecorder.getOldCallStack().isEmpty());
        assertTrue(FunctionCallRecorder.getFinalCallStack().isEmpty());
    }
}