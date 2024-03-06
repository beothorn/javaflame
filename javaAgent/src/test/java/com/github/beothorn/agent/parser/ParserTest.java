package com.github.beothorn.agent.parser;

import org.junit.jupiter.api.Test;


class ParserTest {

    @Test
    void simpleParse(){
        String expression = "foo";
    }

    void complexParse(){
        String expression = "!bla&&(named(foo.bar.Baz)||nameMatches(.*foo.*))";
        String expression2 = "(named(foo.bar.Baz)||nameMatches(.*foo.*))&&!nameEndsWith(bla)";
    }

}