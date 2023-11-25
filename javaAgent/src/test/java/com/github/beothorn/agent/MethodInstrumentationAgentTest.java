package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.github.beothorn.agent.MethodInstrumentationAgent.Flag.allFlagsOnArgument;
import static com.github.beothorn.agent.MethodInstrumentationAgent.LogLevel.*;
import static org.junit.jupiter.api.Assertions.*;

class MethodInstrumentationAgentTest {

    @Test
    void listFlags(){
        assertArrayEquals(new String[]{"no_constructor"},
                allFlagsOnArgument("dtailed,no_constructor,foobar"));
        assertArrayEquals(new String[]{"no_capturing_values", "no_constructor"},
                allFlagsOnArgument("dtailed,no_constructor,no_capturing_values,foobar"));
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
    void whenItHasNoConstructor(){
        whenItHasCommand(
            MethodInstrumentationAgent::argumentHasNoConstructorMode,
            "no_constructor"
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
    void whenItHasExcludes(){
        testArgumentsForExcludes("", new String[][]{});
        testArgumentsForExcludes("xxx", new String[][]{});
        testArgumentsForExcludes("exclude:foo.bar", new String[][]{
            {"foo.bar"}
        });
        testArgumentsForExcludes("exclude:foo.bar,xxx", new String[][]{
                {"foo.bar"}
        });
        testArgumentsForExcludes("xxx,exclude:foo.bar", new String[][]{
                {"foo.bar"}
        });
        testArgumentsForExcludes("exclude:foo.bar,exclude:bar.baz", new String[][]{
                {"foo.bar"},
                {"bar.baz"}
        });
        testArgumentsForExcludes("exclude:foo.bar,fda,exclude:bar.baz,acd,wfae", new String[][]{
                {"foo.bar"},
                {"bar.baz"}
        });
    }

    @Test
    void whenItHasFilter(){
        testArgumentsForFilter("", new String[][]{});
        testArgumentsForFilter("xxx", new String[][]{});
        testArgumentsForFilter("filter:foo.bar", new String[][]{
            {"foo.bar"}
        });
        testArgumentsForFilter("filter:foo.bar,xxx", new String[][]{
            {"foo.bar"}
        });
        testArgumentsForFilter("xxx,filter:foo.bar", new String[][]{
            {"foo.bar"}
        });
        testArgumentsForFilter("filter:foo.bar,filter:bar.baz", new String[][]{
            {"foo.bar"},
            {"bar.baz"},
        });
        testArgumentsForFilter("aaa:bbb:ccc,filter:foo.bar,fda,filter:bar.baz,acd,wfae", new String[][]{
            {"foo.bar"},
            {"bar.baz"},
        });
        testArgumentsForFilter("aaa:bbb:ccc,filter:foo.bar:baz,fda,filter:bar.baz,acd,wfae", new String[][]{
            {"foo.bar", "baz"},
            {"bar.baz"},
        });
    }

    private static void testArgumentsForFilter(String stringArgument, String[][] expectation) {
        List<String[]> args = MethodInstrumentationAgent.argumentFilter(stringArgument);
        testArguments(expectation, args);
    }

    private static void testArgumentsForExcludes(String stringArgument, String[][] expectation) {
        List<String[]> args = MethodInstrumentationAgent.argumentExcludes(stringArgument);
        testArguments(expectation, args);
    }

    private static void testArguments(String[][] expectation, List<String[]> args) {
        assertEquals(expectation.length, args.size());
        for (int i = 0; i < expectation.length; i++) {
            String[] arg = args.get(i);
            assertEquals(expectation[i][0], arg[0]);
            if (expectation[i].length > 1) {
                assertEquals(expectation[i][1], arg[1]);
            }
        }
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

}