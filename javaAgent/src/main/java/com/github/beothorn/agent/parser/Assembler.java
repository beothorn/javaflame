package com.github.beothorn.agent.parser;

import java.util.List;

public interface Assembler<T> {
    T assembleDefaultMatcher(Token token) throws CompilationException;
    T assembleDeclaredMatcher(Token functionToken, String stringArgument) throws CompilationException;
    T assemble(Token token, List<T> args) throws CompilationException;
}
