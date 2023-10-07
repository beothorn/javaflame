package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import static com.github.beothorn.agent.SpanCatcher.onEnter;
import static com.github.beothorn.agent.SpanCatcher.onLeave;
import static org.junit.jupiter.api.Assertions.*;

class SpanCatcherTest {

    @Test
    void happyDay(){
        SpanCatcher.stackPerThread.clear();
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
        "}";
        String threadMain = "{" +
            "\"thread\":\"main\"," +
            "\"snapshotTime\":0,"+
            "\"span\":{" +
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
        "}";
        assertEquals(
            "[" + threadT + "," + threadMain +"]",
            SpanCatcher.getFinalCallStack()
                    .replaceAll("\n", "")
                    .replaceAll("\"snapshotTime\":[0-9]+,", "\"snapshotTime\":0,")
        );
    }
}