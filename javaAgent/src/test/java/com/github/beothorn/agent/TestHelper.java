package com.github.beothorn.agent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestHelper {

    public static JSONObject thread(
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

    public static JSONObject span(
            String name,
            int entryTime,
            int exitTime,
            int value,
            String[][] arguments,
            JSONObject... children
    ){
        try {
            JSONObject result = new JSONObject()
                .put("name", name)
                .put("entryTime", entryTime)
                .put("exitTime", exitTime)
                .put("value", value);

            JSONArray argumentsArray = new JSONArray();
            for(String[] argument : arguments) {
                argumentsArray.put(
                    new JSONObject()
                        .put("type", argument[0])
                        .put("value", argument[1])
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

    public static JSONObject span(
        String name,
        int entryTime,
        int exitTime,
        int value,
        JSONObject... children
    ){
        try {
            JSONObject result = new JSONObject()
                .put("name", name)
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
}
