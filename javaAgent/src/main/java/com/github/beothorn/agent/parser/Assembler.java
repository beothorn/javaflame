package com.github.beothorn.agent.parser;

public interface Assembler {
    Object assemble(Token token);
    Object assemble(Token token, Object[] args);
}
