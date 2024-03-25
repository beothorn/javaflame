package com.github.beothorn.tests;

public class TestFiltersA {

    public String functionAAA(){
        new TestFiltersC().functionABC();
        return "AAA";
    }

    public String functionBBB(){
        new TestFiltersC().functionABC();
        return "BBB";
    }

    public String functionCCC(){
        new TestFiltersC().functionABC();
        return "CCC";
    }
}
