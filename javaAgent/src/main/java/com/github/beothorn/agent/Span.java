package com.github.beothorn.agent;

import java.util.*;

public class Span{

    private final String name;
    private Span parent;
    private List<Span> children;
    public final long entryTime;
    public long exitTime;

    public static Span span(
        final String name,
        final long entryTime
    ){
        return new Span(
            name,
            entryTime,
            -1,
            null,
            new ArrayList<>()
        );
    }

    public static Span span(
        final String name,
        final long entryTime,
        final Span parent
    ){
        return new Span(
            name,
            entryTime,
            -1,
            parent,
            new ArrayList<>()
        );
    }

    public static Span span(
            final String name,
            final long entryTime,
            final long exitTime,
            final List<Span> children
    ){
        return new Span(
                name,
                entryTime,
                exitTime,
                null,
                children
        );
    }

    public static Span span(
            final String name,
            final long entryTime,
            final List<Span> children
    ){
        return new Span(
                name,
                entryTime,
                -1,
                null,
                children
        );
    }

    public static Span span(
            final String name,
            final long entryTime,
            final long exitTime
    ){
        return new Span(
                name,
                entryTime,
                exitTime,
                null,
                new ArrayList<>()
        );
    }

    private Span(
        final String name,
        final long entryTime,
        final long exitTime,
        final Span parent,
        final List<Span> children
    ){
        this.name = name;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.parent = parent;
        this.children = new ArrayList<>(children);
        this.children.forEach(c -> c.parent = this);
    }

    public Span enter(
        final String methodName,
        final long entryTime
    ){
        Span child = span(methodName, entryTime, this);
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

    public Span leave(final long exitTime){
        this.exitTime = exitTime;
        return parent;
    }

    private long duration(){
        if(exitTime == -1){
            // If has not exited yet, 
            return 0;
        }
        return exitTime - entryTime;
    }

    public String toJson(){
        String nameEscaped = name
                .replaceAll("\"", "\\\\\"")
                .replaceAll("\n", "\\\\n")
                .replaceAll("\t", "\\\\t");
        if(children.isEmpty()){
            return "{" +
                        "\"name\":\""+ nameEscaped +"\"," +
                        "\"entryTime\":"+ entryTime +"," +
                        "\"exitTime\":"+ exitTime +"," +
                        "\"value\":"+ duration() +
                    "}\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(children.get(0).toJson());
        for (int i = 1; i < children.size(); i++) {
            sb.append(",");
            sb.append(children.get(i).toJson());
        }

        String childrenAsJson = sb.toString();
        return "{" +
                    "\"name\":\""+ nameEscaped +"\"," +
                    "\"entryTime\":"+ entryTime +"," +
                    "\"exitTime\":"+ exitTime +"," +
                    "\"value\":"+ duration() +"," +
                    "\"children\":["+childrenAsJson+"]"+
                "}\n";
    }

    public String description(){
        return name + ": "+ duration();
    }

    @Override
    public String toString() {
        if(children.isEmpty()) return name+": "+ duration();
        return name+": "+ duration() +" "+Arrays.toString(children.toArray());
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
            return entryTime == span.entryTime && Objects.equals(name, span.name);
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
        return Objects.hash(name, entryTime, children);
    }

    /***
     * Remove old spans and keep only the current active span.
     * This is the last child branch.
     *
     * @return the non active branch
     */
    public Optional<Span> removeFinishedFunction(){
        if(children.isEmpty()){
            // If it has no children, there is nothing to remove
            // return nothing removed
            return Optional.empty();
        }

        // The last child is the active, all others will be removed
        Span activeChild = children.remove(children.size() - 1);

        if(activeChild.exitTime != -1){
            // If active is also done, remove all children
            List<Span> oldChildren = children;
            oldChildren.add(activeChild);
            children = new ArrayList<>();
            return Optional.of(new Span(
                    name,
                    entryTime,
                    exitTime,
                    parent,
                    oldChildren
            ));
        }

        // We also want the active child to remove old spans
        // The spans for the other children are all old, only the active child may keep some
        Optional<Span> activeChildrenPastSpans = activeChild.removeFinishedFunction();

        boolean thereIsNoMoreChildren = children.isEmpty();
        boolean noSpanWasRemoved = activeChildrenPastSpans.isEmpty();
        boolean thereAreNoOldSpansToReturn = thereIsNoMoreChildren && noSpanWasRemoved;

        if(thereAreNoOldSpansToReturn){
            // nothing changed, put child back
            children.add(activeChild);
            return Optional.empty();
        }

        List<Span> oldChildren = children;
        activeChildrenPastSpans.map(oldChildren::add);
        children = new ArrayList<>();
        children.add(activeChild);
        return Optional.of(new Span(
            name,
            entryTime,
            exitTime,
            null,
            oldChildren
        ));
    }
}
