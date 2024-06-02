package com.github.beothorn.tests;

public class TestFiltersA {

    public String functionAAA(){
        new TestFiltersC("ARGC").functionABC();
        return "AAA";
    }

    public String functionBBB(){
        new TestFiltersC("BBBARGS").functionABC();
        return "BBB";
    }

    public String functionNonStatic(String x){
        return x;
    }

    public String functionCCC(){
        new TestFiltersC("CCCARGS").functionABC();
        return "CCC";
    }

    public void threadLoop(){
        System.out.println("Waiting to be terminated manually");
    }

    @Override
    public String toString() {
        return "TestA";
    }
}
