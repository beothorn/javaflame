package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static com.github.beothorn.agent.MethodInstrumentationAgent.Flag.allFlagsOnArgument;
import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.*;
import static org.junit.jupiter.api.Assertions.*;

class MethodInstrumentationAgentTest {

    @Test
    void listFlags(){
        assertArrayEquals(new String[]{"core_classes"},
                allFlagsOnArgument("dtailed,core_classes,foobar"));
        assertArrayEquals(new String[]{"no_capturing_values", "core_classes"},
                allFlagsOnArgument("dtailed,core_classes,no_capturing_values,foobar"));
    }

    void whenItHasCommand(Function<String, Boolean> test, String command){
        assertTrue(test.apply(command + ",asdasd"));
        assertTrue(test.apply("asdas," + command));
        assertTrue(test.apply("asdas," + command + ",asdasd"));
        assertTrue(test.apply(command));
        assertFalse(test.apply("include:com."+command+".foobar,modse:"+command));
        assertFalse(test.apply("asdas," + command + ":no_capturing_values"));
    }

    @Test
    void whenItHasCommandNoCapturingValue(){
        whenItHasCommand(
            MethodInstrumentationAgent::argumentHasNoCaptureValuesMode,
            "no_capturing_values"
        );
    }

    @Test
    void whenItHasCommandQualifiedFunctionsValue(){
        whenItHasCommand(
            MethodInstrumentationAgent::argumentHasQualifiedFunctions,
            "qualified_functions"
        );
    }

    @Test
    void whenItHasIncludeCoreClasses(){
        whenItHasCommand(
            MethodInstrumentationAgent::argumentHasIncludeCoreClasses,
            "core_classes"
        );
    }

    @Test
    void whenItHasNoSnapshots(){
        whenItHasCommand(
                MethodInstrumentationAgent::argumentHasNoSnapshotsMode,
                "no_snapshots"
        );
    }

    @Test
    void argumentLogLevel(){
        assertEquals(INFO,MethodInstrumentationAgent.argumentLogLevel("asdasd"));
        assertEquals(NONE,MethodInstrumentationAgent.argumentLogLevel("asdasd,log:NONE"));
        assertEquals(DEBUG,MethodInstrumentationAgent.argumentLogLevel("log:DEBUG,dsf"));
        assertEquals(DEBUG,MethodInstrumentationAgent.argumentLogLevel("safas,log:DEBUG,dsf"));
    }

    @Test
    void logLevelPrintHierarchy(){
        assertFalse(NONE.shouldPrint(ERROR));
        assertFalse(NONE.shouldPrint(INFO));
        assertFalse(NONE.shouldPrint(WARN));
        assertFalse(NONE.shouldPrint(DEBUG));


        assertTrue(ERROR.shouldPrint(ERROR));
        assertFalse(ERROR.shouldPrint(INFO));
        assertFalse(ERROR.shouldPrint(WARN));
        assertFalse(ERROR.shouldPrint(DEBUG));

        assertTrue(INFO.shouldPrint(ERROR));
        assertTrue(INFO.shouldPrint(INFO));
        assertFalse(INFO.shouldPrint(WARN));
        assertFalse(INFO.shouldPrint(DEBUG));

        assertTrue(WARN.shouldPrint(ERROR));
        assertTrue(WARN.shouldPrint(INFO));
        assertTrue(WARN.shouldPrint(WARN));
        assertFalse(WARN.shouldPrint(DEBUG));

        assertTrue(DEBUG.shouldPrint(ERROR));
        assertTrue(DEBUG.shouldPrint(INFO));
        assertTrue(DEBUG.shouldPrint(WARN));
        assertTrue(DEBUG.shouldPrint(DEBUG));
    }

    @Test
    void commandFile(){
        assertEquals(
            "C:/foo/bar",
            MethodInstrumentationAgent.outputFileOnArgument("out:C:/foo/bar").orElseThrow(RuntimeException::new)
        );
        assertEquals(
                "C:/foo/bar",
                MethodInstrumentationAgent.outputFileOnArgument("mode:xyz,out:C:/foo/bar").orElseThrow(RuntimeException::new)
        );
        assertEquals(
                "C:/foo/bar",
                MethodInstrumentationAgent.outputFileOnArgument("out:C:/foo/bar,mode:xyz").orElseThrow(RuntimeException::new)
        );
        assertEquals(
                "C:/foo/bar",
                MethodInstrumentationAgent.outputFileOnArgument("mode:abc,out:C:/foo/bar,mode:xyz").orElseThrow(RuntimeException::new)
        );
    }

    @Test
    void whenItHasFilter(){
        testArgumentsForFilter("", Optional.empty());
        testArgumentsForFilter("xxx", Optional.empty());
        testArgumentsForFilter("filter:foo.bar", Optional.of("foo.bar"));
        testArgumentsForFilter("filter:foo.bar,xxx", Optional.of("foo.bar"));
        testArgumentsForFilter("xxx,filter:foo.bar", Optional.of("foo.bar"));
    }

    private static void testArgumentsForFilter(String stringArgument, Optional<String> expectation) {
        Optional<String> actual = MethodInstrumentationAgent.argumentFilter(stringArgument);
        assertEquals(expectation, actual);
    }

    @Test
    void whenItHasStartRecordingFunction(){
        Optional<String> maybeStartRecordingTrigger =  MethodInstrumentationAgent
            .argumentStartRecordingTriggerFunction(
                "startRecordingTriggerFunction:Foo.onStart,stopRecordingTriggerFunction:Foo.onEnd"
            );
        assertTrue(maybeStartRecordingTrigger.isPresent());
        assertEquals("Foo.onStart", maybeStartRecordingTrigger.get());
    }

    @Test
    void whenItHasStopRecordingFunction(){
        Optional<String> maybeStopRecordingTrigger =  MethodInstrumentationAgent
            .argumentStopRecordingTriggerFunction(
                "startRecordingTriggerFunction:Foo.onStart,stopRecordingTriggerFunction:Foo.onEnd"
            );
        assertTrue(maybeStopRecordingTrigger.isPresent());
        assertEquals("Foo.onEnd", maybeStopRecordingTrigger.get());
    }

    @Test
    void metadataRendersCorrectly(){
        String argument = "filter:x.y,"
                + "startRecordingTriggerFunction:onStart,"
                + "stopRecordingTriggerFunction:onEnd";
        /*
         * I know these are bad tests, exposing internals of the MethodInstrumentationAgent class
         * and replicating business logic on tests, but I started this project with as little
         * dependencies as possible, and now it is kinda late to switch (unless I happen to get
         * a lot of free time).
         * The right way here is to use mockito and test all at once.
         * We could refactor the code and isolate the logic, but I really do not want to introduce
         * too many classes, nor I want to return some dynamic structure with the data.
         * I already did it with types and values, it felt wrooonnng.
         */
        Optional<String> filter = MethodInstrumentationAgent.argumentFilter(argument);
        Optional<String> maybeStartRecordingTriggerFunction = MethodInstrumentationAgent.argumentStartRecordingTriggerFunction(argument);
        Optional<String> maybeStopRecordingTriggerFunction = MethodInstrumentationAgent.argumentStopRecordingTriggerFunction(argument);

        String[] allFlags = allFlagsOnArgument(argument);
        String allFlagsAsString = Arrays.toString(allFlags);
        String outputDirectory = "/foo/bar";

        String actual = MethodInstrumentationAgent.getExecutionMetadataAsHtml(
            allFlagsAsString,
            outputDirectory,
            filter,
            maybeStartRecordingTriggerFunction,
            maybeStopRecordingTriggerFunction
        );

        String expected = "<p>Flags: []</p>" +
                "<p>Output: '/foo/bar'</p>" +
                "<p>Filters: x.y</p>" +
                "<p>Start recording trigger function: 'onStart'</p>" +
                "<p>Stop recording trigger function: 'onEnd'</p>";

        assertEquals(expected, actual);
    }
}