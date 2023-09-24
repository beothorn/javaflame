package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import static com.github.beothorn.agent.SpanCatcher.onEnter;
import static com.github.beothorn.agent.SpanCatcher.onLeave;
import static org.junit.jupiter.api.Assertions.*;

class SpanCatcherTest {

    @Test
    void happyDay(){
        SpanCatcher.stackPerThread.clear();
        onEnter("main", "a"); // main: a
        onEnter("main", "aa"); // main: a -> aa
        onLeave("main", 0, 10); // main: a
        onEnter("main", "ab"); // main: a -> ab
        onEnter("t", "a"); // t: a
        onEnter("t", "b"); // t: a -> b
        onLeave("t", 10, 20); // t: a
        onLeave("main", 10, 20); // main: a
        onEnter("main", "ac"); // main: a -> ac
        onEnter("main", "aca"); // main: a -> ac -> aca
        onLeave("main", 20, 30); // main: a -> ac
        onLeave("t", 20, 30);  // t: no root
        onLeave("main", 30, 40); // main: a

        assertEquals("[" +
                "{\"thread\":\"t\",\"span\":{\"children\":[{\"name\":\"b\",\"value\":10}],\"name\":\"a\",\"value\":10}}," +
                "{\"thread\":\"main\",\"span\":{\"children\":[{\"name\":\"aa\",\"value\":10},{\"name\":\"ab\",\"value\":10},{\"children\":[{\"name\":\"aca\",\"value\":10}],\"name\":\"ac\",\"value\":10}],\"name\":\"a\",\"value\":0}}]",
                SpanCatcher.getFinalCallStack());
    }
}