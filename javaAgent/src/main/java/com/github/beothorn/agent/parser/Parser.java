package com.github.beothorn.agent.parser;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class Parser {
    public ElementMatcher.Junction<TypeDescription> parse(String expression){
        /*
        todo: named namedIgnoreCase nameStartsWith nameStartsWithIgnoreCase nameEndsWith
        nameEndsWithIgnoreCase nameContains nameContainsIgnoreCase
         nameMatches <- regex

         */
        return nameContains(expression);
    }
}
