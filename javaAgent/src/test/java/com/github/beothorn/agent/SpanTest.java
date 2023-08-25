package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpanTest {

    @Test
    void happyDayJson(){
        Span subject = Span.span("foo", 10, List.of(
                Span.span("fooA", 5, List.of()),
                Span.span("fooB", 5, List.of())
        ));
        String expected = "{\"children\":[{\"name\":\"fooA\",\"value\":5},{\"name\":\"fooB\",\"value\":5}],\"name\":\"foo\",\"value\":10}";
        assertEquals(expected, subject.toJson());
    }

}