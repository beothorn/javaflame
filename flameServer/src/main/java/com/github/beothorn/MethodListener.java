package com.github.beothorn;

public class MethodListener {

    public static void onEnter(String methodName){
        System.out.println("Received method signal" + methodName );
    }

}
