package com.github.beothorn.agent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MethodInstrumentationAgentTest {

    @Test
    void whenItHasCommandDetailed(){
        assertTrue(MethodInstrumentationAgent.argumentHasDetailedMode("mode:detailed,asdasd"));
        assertTrue(MethodInstrumentationAgent.argumentHasDetailedMode("asdas,mode:detailed"));
        assertTrue(MethodInstrumentationAgent.argumentHasDetailedMode("asdas,mode:detailed,asdasd"));
        assertTrue(MethodInstrumentationAgent.argumentHasDetailedMode("mode:detailed"));
        assertFalse(MethodInstrumentationAgent.argumentHasDetailedMode("asdas,modse:detailed"));
    }

    @Test
    void whenItHasCommandDebug(){
        assertTrue(MethodInstrumentationAgent.argumentHasDebugMode("mode:debug,asdasd"));
        assertTrue(MethodInstrumentationAgent.argumentHasDebugMode("asdas,mode:debug"));
        assertTrue(MethodInstrumentationAgent.argumentHasDebugMode("asdas,mode:debug,asdasd"));
        assertTrue(MethodInstrumentationAgent.argumentHasDebugMode("mode:debug"));
        assertFalse(MethodInstrumentationAgent.argumentHasDebugMode("asdas,modse:debug"));
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

}