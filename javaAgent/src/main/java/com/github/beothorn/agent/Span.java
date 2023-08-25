package com.github.beothorn.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Span{
    private final String name;
    private long value;
    private final List<Span> children;

    public static Span span(final String name){
        return new Span(name, 0, new ArrayList<>());
    }

    public static Span span(final String name, final int value, final List<Span> children){
        return new Span(name, value, children);
    }

    private Span(final String name, final int value, final List<Span> children){
        this.name = name;
        this.value = value;
        this.children = children;
    }

    public void value(final long value){
        this.value = value;
    }

    public void add(final Span children){
        this.children.add(children);
    }

    public String toJson(){
        if(children.isEmpty()){
            return "{\"name\":\""+name+"\",\"value\":"+value+"}";
        }
        String childrenAsJson = children.stream()
                .map(Span::toJson)
                .collect(Collectors.joining(","));
        return "{\"children\":["+childrenAsJson+"],\"name\":\""+name+"\",\"value\":"+value+"}";
    }

    @Override
    public String toString() {
        return toJson();
    }
}
