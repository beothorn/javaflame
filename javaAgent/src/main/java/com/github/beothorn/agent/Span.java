package com.github.beothorn.agent;

import java.util.*;

public class Span{

    private final String name;
    private Span parent;
    private List<Span> children;
    public final long entryTime;
    public long exitTime;
    // Type -> value
    public String[] returnValue;
    public final String[][] arguments;

    public static Span span(
            final String name,
            final long entryTime
    ){
        return new Span(
            name,
            entryTime,
            null,
            -1,
            null,
            new ArrayList<>()
        );
    }

    public static Span span(
        final String name,
        final long entryTime,
        final String[][] arguments
    ){
        return new Span(
            name,
            entryTime,
            arguments,
            -1,
            null,
            new ArrayList<>()
        );
    }

    public static Span span(
        final String name,
        final long entryTime,
        final String[][] arguments,
        final Span parent
    ){
        return new Span(
            name,
            entryTime,
            arguments,
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
                null,
                exitTime,
                null,
                children
        );
    }

    public static Span span(
            final String name,
            final long entryTime,
            final String[][] arguments,
            final long exitTime,
            final List<Span> children
    ){
        return new Span(
                name,
                entryTime,
                arguments,
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
            null,
            -1,
            null,
            children
        );
    }

    public static Span span(
            final String name,
            final long entryTime,
            final String[][] arguments,
            final List<Span> children
    ){
        return new Span(
                name,
                entryTime,
                arguments,
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
            null,
            exitTime,
            null,
            new ArrayList<>()
        );
    }

    public static Span span(
            final String name,
            final long entryTime,
            final String[][] arguments,
            final long exitTime
    ){
        return new Span(
                name,
                entryTime,
                arguments,
                exitTime,
                null,
                new ArrayList<>()
        );
    }

    private Span(
        final String name,
        final long entryTime,
        final String[][] arguments,
        final long exitTime,
        final Span parent,
        final List<Span> children
    ){
        this.name = name;
        this.entryTime = entryTime;
        this.arguments = arguments;
        this.exitTime = exitTime;
        this.parent = parent;
        this.children = new ArrayList<>(children);
        this.children.forEach(c -> c.parent = this);
    }

    public Span enter(
        final String methodName,
        final long entryTime
    ){
        return enter(
            methodName,
            entryTime,
            null
        );
    }

    public Span enter(
        final String methodName,
        final long entryTime,
        final String[][] arguments
    ){
        Span child = span(
            methodName,
            entryTime,
            arguments,
            this
        );
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
        return leave(exitTime, null);
    }

    /**
     * To be called after the span has exited.
     * @param exitTime The exit timestamp of the span.
     * @param returnValue An array with two elements, the return type and the return value.
     * @return
     */
    public Span leave(
            final long exitTime,
            final String[] returnValue
    ){
        this.exitTime = exitTime;
        this.returnValue = returnValue;
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
        String nameMaybeWithReturn = name;
        if (returnValue != null) {
            if(returnValue[0].equals("void")) {
                nameMaybeWithReturn = name + " => " + returnValue[0];
            } else {
                nameMaybeWithReturn = name + " => " + returnValue[0] + " " + returnValue[1];
            }
        }
        String nameEscaped = escapeString(nameMaybeWithReturn);

        StringBuilder result = new StringBuilder("{" +
            "\"name\":\""+ nameEscaped +"\"," +
            "\"entryTime\":"+ entryTime +"," +
            "\"exitTime\":"+ exitTime +"," +
            "\"value\":"+ duration()
        );

        if(arguments != null && arguments.length > 0){
            result.append(",\"arguments\":[");
            String[] firstArgument = arguments[0];
            appendTypedValue(result, firstArgument);

            for(int i =1; i < arguments.length; i++){
                String[] argument = arguments[i];
                result.append(",");
                appendTypedValue(result, argument);
            }
            result.append("]");
        }

        if (returnValue != null) {
            result.append(",\"return\":");
            appendTypedValue(result, returnValue);
        }

        if(children.isEmpty()){
            return result.append("}\n").toString();
        }

        result.append(",\"children\":[")
            .append(children.get(0).toJson());

        for (int i = 1; i < children.size(); i++) {
            result.append(",");
            result.append(children.get(i).toJson());
        }

        result.append("]}\n");
        return result.toString();
    }

    public static String escapeString(String nameMaybeWithReturn) {
        if(nameMaybeWithReturn == null) return "null";
        return nameMaybeWithReturn
                .replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\"", "\\\\\"")
                .replaceAll("\n", "\\\\n")
                .replaceAll("\r", "\\\\r")
                .replaceAll("\t", "\\\\t");
    }

    private static void appendTypedValue(StringBuilder result, String[] argument) {
        result.append("{\"type\":\"")
            .append(argument[0])
            .append("\",\"value\":\"")
            .append(escapeString(argument[1]))
            .append("\"}");
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
                    arguments,
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
            arguments,
            exitTime,
            null,
            oldChildren
        ));
    }
}
