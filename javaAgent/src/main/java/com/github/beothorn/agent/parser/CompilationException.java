package com.github.beothorn.agent.parser;

public class CompilationException extends Exception{

    public CompilationException(int position, String input, String error){
        super(error + ": \"" + input.substring(0, position)
                + "[" + input.charAt(position)
                + "]" + input.substring(position + 1)
                + "\"");
    }

}
