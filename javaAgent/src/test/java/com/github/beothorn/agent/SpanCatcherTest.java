package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpanCatcherTest {

    @Test
    void happyDay(){
        SpanCatcher.onEnter("a");
        SpanCatcher.onEnter("aa");
        SpanCatcher.onLeave(0, 10);
        SpanCatcher.onEnter("ab");
        SpanCatcher.onLeave(10, 20);
        SpanCatcher.onEnter("ac");
        SpanCatcher.onEnter("aca");
        SpanCatcher.onLeave(20, 30);
        SpanCatcher.onLeave(30, 40);

        assertEquals("{\"thread\":\"main\",\"span\":" +
                "{\"children\":[" +
                    "{\"name\":\"aa\",\"value\":10}," +
                    "{\"name\":\"ab\",\"value\":10}," +
                    "{\"children\":[" +
                        "{\"name\":\"aca\",\"value\":10}]," +
                    "\"name\":\"ac\",\"value\":10}]," +
                "\"name\":\"a\",\"value\":0}}", SpanCatcher.getFinalCallStack());
    }
}