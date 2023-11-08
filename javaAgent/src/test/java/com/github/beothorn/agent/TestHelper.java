package com.github.beothorn.agent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static JSONObject threadJSON(
        String thread,
        int snapshotTime,
        JSONObject span
    ){
        try {
            return new JSONObject()
                .put("thread", thread)
                .put("snapshotTime", snapshotTime)
                .put("span", span);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject spanJSON(
            String name,
            String method,
            int entryTime,
            int exitTime,
            int value,
            String[][] arguments,
            JSONObject... children
    ){
        try {
            JSONObject result = new JSONObject()
                .put("name", name)
                .put("method", method)
                .put("entryTime", entryTime)
                .put("exitTime", exitTime)
                .put("value", value);

            JSONArray argumentsArray = new JSONArray();
            for(String[] argument : arguments) {
                argumentsArray.put(
                        argumentJSON(argument)
                );
            }

            result.put("arguments", argumentsArray);

            if (children.length > 0) {
                result.put("children", new JSONArray(children));
            }
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject argumentJSON(String[] argument) throws JSONException {
        return new JSONObject()
                .put("type", argument[0])
                .put("value", argument[1]);
    }

    public static JSONObject spanJSON(
        String name,
        String method,
        int entryTime,
        int exitTime,
        int value,
        JSONObject... children
    ){
        try {
            JSONObject result = new JSONObject()
                .put("name", name)
                .put("method", method)
                .put("entryTime", entryTime)
                .put("exitTime", exitTime)
                .put("value", value);
            if (children.length > 0) {
                result.put("children", new JSONArray(children));
            }
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Span spanTest(
            final String name,
            final String method,
            final long entryTime
    ){
        return new Span(
            name,
            method,
            entryTime,
            null,
            -1,
            null,
            new ArrayList<>()
        );
    }

    public static Span spanTest(
            final String name,
            final String method,
            final long entryTime,
            final long exitTime
    ){
        return new Span(
            name,
            method,
            entryTime,
            null,
            exitTime,
            null,
            new ArrayList<>()
        );
    }

    public static Span spanTest(
            final String name,
            final String method,
            final long entryTime,
            final long exitTime,
            final List<Span> children
    ){
        return new Span(
                name,
                method,
                entryTime,
                null,
                exitTime,
                null,
                children
        );
    }

    public static Span spanTest(
            final String name,
            final String method,
            final long entryTime,
            final String[][] arguments,
            final long exitTime,
            final List<Span> children
    ){
        return new Span(
                name,
                method,
                entryTime,
                arguments,
                exitTime,
                null,
                children
        );
    }

    public static Span spanTest(
            final String name,
            final String method,
            final long entryTime,
            final List<Span> children
    ){
        return new Span(
            name,
            method,
            entryTime,
            null,
            -1,
            null,
            children
        );
    }
}
