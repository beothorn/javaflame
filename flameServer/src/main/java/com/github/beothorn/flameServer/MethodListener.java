package com.github.beothorn.flameServer;

public class MethodListener {

    public static void onEnter(String methodName){
        String threadName = Thread.currentThread().getName();
        System.out.println("["+threadName+"] -> " + methodName );
    }

    public static void onLeave(String methodName, Long executionTime){
        String threadName = Thread.currentThread().getName();
        System.out.println("["+threadName+"] <- ("+executionTime+") " + methodName );
    }

}
