package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpanCatcherTest {

    @Test
    void happyDay(){
        SpanCatcher.onEnter("main", "a");
        SpanCatcher.onEnter("main", "aa");
        SpanCatcher.onLeave("main", 0, 10);
        SpanCatcher.onEnter("main", "ab");
        SpanCatcher.onEnter("t", "a");
        SpanCatcher.onEnter("t", "b");
        SpanCatcher.onLeave("t", 10, 20);
        SpanCatcher.onLeave("main", 10, 20);
        SpanCatcher.onEnter("main", "ac");
        SpanCatcher.onEnter("main", "aca");
        SpanCatcher.onLeave("main", 20, 30);
        SpanCatcher.onLeave("t", 20, 30);
        SpanCatcher.onLeave("main", 30, 40);

        assertEquals("[" +
                "{\"thread\":\"t\",\"span\":{\"children\":[{\"name\":\"b\",\"value\":10}],\"name\":\"a\",\"value\":10}}," +
                "{\"thread\":\"main\",\"span\":{\"children\":[{\"name\":\"aa\",\"value\":10},{\"name\":\"ab\",\"value\":10},{\"children\":[{\"name\":\"aca\",\"value\":10}],\"name\":\"ac\",\"value\":10}],\"name\":\"a\",\"value\":0}}]", SpanCatcher.getFinalCallStack());
    }
}