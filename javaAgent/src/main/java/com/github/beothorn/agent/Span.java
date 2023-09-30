package com.github.beothorn.agent;

import java.util.*;
import java.util.stream.Collectors;

public class Span{
    private final String name;
    private long value;
    private Span parent;
    private List<Span> children;
    public long start;

    public static Span span(final String name){
        return new Span(0, name, 0, new ArrayList<>());
    }

    public static Span span(final long start, final String name){
        return new Span(start, name, 0, new ArrayList<>());
    }

    public static Span span(final String name, final long value, final List<Span> children){
        return new Span(0, name, value, children);
    }

    public static Span span(final int start, final String name, final long value, final List<Span> children){
        return new Span(start, name, value, children);
    }

    private Span(final long start, final String name, final long value, final List<Span> children){
        this.start = start;
        this.name = name;
        this.value = value;
        this.children = new ArrayList<>(children);
    }

    public void value(final long value){
        this.value = value;
    }

    public Span enter(final String methodName){
        // Sum child values here
        Span child = span(methodName);
        child.parent = this;
        children.add(child);
        return child;
    }

    public Span getRoot(){
        Span root = this;
        while(root.parent != null){
            root = root.parent;
        }
        return root;
    }

    public Span leave(){
        return parent;
    }

    public String toJson(){
        if(children.isEmpty()){
            return "{" +
                        "\"start\":"+start+"," +
                        "\"name\":\""+name+"\"," +
                        "\"value\":"+value+
                    "}";
        }
        String childrenAsJson = children.stream()
                .map(Span::toJson)
                .collect(Collectors.joining(","));
        return "{" +
                    "\"start\":\""+start+"\"," +
                    "\"name\":\""+name+"\"," +
                    "\"value\":"+value+"," +
                    "\"children\":["+childrenAsJson+"]"+
                "}";
    }

    public String description(){
        return name + ": "+value;
    }

    @Override
    public String toString() {
        if(children.isEmpty()) return name+": "+value;
        return name+": "+value+" "+Arrays.toString(children.toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Span span = (Span) o;
        if(span.children.size() != children.size()){
            return false;
        }

        if(children.isEmpty()){
            return value == span.value && Objects.equals(name, span.name);
        }

        for (int i = 0; i < children.size(); i++) {
            if(!children.get(i).equals(span.children.get(i))){
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, children);
    }

    public Optional<Span> removePastSpans(){
        if(children.isEmpty()){
            return Optional.empty();
        }

        Span activeChild = children.remove(children.size() - 1);

        Optional<Span> activeChildrenPastSpans = activeChild.removePastSpans();

        if(!children.isEmpty() || activeChildrenPastSpans.isPresent()){
            List<Span> oldChildren = children;
            activeChildrenPastSpans.map(oldChildren::add);
            children = Arrays.asList(activeChild);
            return Optional.of(span(name, value, oldChildren));
        }

        // nothing changed
        children = Arrays.asList(activeChild);
        return Optional.empty();
    }
}
