package com.github.beothorn.agent.parser;

import java.util.List;

public interface Assembler<T> {
    T assemble(Token token);
    T assemble(Token token, List<T> args);
}
