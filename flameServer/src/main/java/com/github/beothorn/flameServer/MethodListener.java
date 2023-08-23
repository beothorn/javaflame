package com.github.beothorn.flameServer;

public class MethodListener {

    public static void onEnter(String methodName){
        System.out.println("Received method signal" + methodName );
    }

    public static void onLeave(String methodName, Long executionTime){
        System.out.println("Received method exit signal" + methodName + " took " + executionTime );
    }

}
