package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
            MethodInstrumentationAgent.outputFileOnArgument("out:C:/foo/bar").orElseThrow()
        );
        assertEquals(
                "C:/foo/bar",
                MethodInstrumentationAgent.outputFileOnArgument("mode:xyz,out:C:/foo/bar").orElseThrow()
        );
        assertEquals(
                "C:/foo/bar",
                MethodInstrumentationAgent.outputFileOnArgument("out:C:/foo/bar,mode:xyz").orElseThrow()
        );
        assertEquals(
                "C:/foo/bar",
                MethodInstrumentationAgent.outputFileOnArgument("mode:abc,out:C:/foo/bar,mode:xyz").orElseThrow()
        );
    }

    @Test
    void whenItHasExcludes(){
        assertEquals("[]", Arrays.toString(MethodInstrumentationAgent.argumentExcludes("").toArray()));
        assertEquals("[]", Arrays.toString(MethodInstrumentationAgent.argumentExcludes("xxx").toArray()));
        assertEquals("[foo.bar]", Arrays.toString(MethodInstrumentationAgent.argumentExcludes("exclude:foo.bar").toArray()));
        assertEquals("[foo.bar]", Arrays.toString(MethodInstrumentationAgent.argumentExcludes("exclude:foo.bar,xxx").toArray()));
        assertEquals("[foo.bar]", Arrays.toString(MethodInstrumentationAgent.argumentExcludes("xxx,exclude:foo.bar").toArray()));
        assertEquals("[foo.bar, bar.baz]", Arrays.toString(MethodInstrumentationAgent.argumentExcludes("exclude:foo.bar,exclude:bar.baz").toArray()));
        assertEquals("[foo.bar, bar.baz]", Arrays.toString(MethodInstrumentationAgent.argumentExcludes("exclude:foo.bar,fda,exclude:bar.baz,acd,wfae").toArray()));
    }

    @Test
    void whenItHasFilter(){
        assertEquals("[]", Arrays.toString(MethodInstrumentationAgent.argumentFilter("").toArray()));
        assertEquals("[]", Arrays.toString(MethodInstrumentationAgent.argumentFilter("xxx").toArray()));
        assertEquals("[foo.bar]", Arrays.toString(MethodInstrumentationAgent.argumentFilter("filter:foo.bar").toArray()));
        assertEquals("[foo.bar]", Arrays.toString(MethodInstrumentationAgent.argumentFilter("filter:foo.bar,xxx").toArray()));
        assertEquals("[foo.bar]", Arrays.toString(MethodInstrumentationAgent.argumentFilter("xxx,filter:foo.bar").toArray()));
        assertEquals("[foo.bar, bar.baz]", Arrays.toString(MethodInstrumentationAgent.argumentFilter("filter:foo.bar,filter:bar.baz").toArray()));
        assertEquals("[foo.bar, bar.baz]", Arrays.toString(MethodInstrumentationAgent.argumentFilter("filter:foo.bar,fda,filter:bar.baz,acd,wfae").toArray()));
    }

}