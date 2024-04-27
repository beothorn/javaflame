package com.github.beothorn.agent.parser;

import java.util.List;



public interface Assembler<T> {

    T assembleDefaultMatcher(
        Token token,
        List<Flag> flags
    ) throws CompilationException;
    T assembleDeclaredMatcher(
        Token functionToken,
        String stringArgument,
        List<Flag> flags
    ) throws CompilationException;
    T assemble(
        Token token,
        List<T> args,
        List<Flag> flags
    ) throws CompilationException;
}
