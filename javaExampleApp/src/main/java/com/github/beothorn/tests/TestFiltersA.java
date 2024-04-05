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

    public String functionCCC(){
        new TestFiltersC("CCCARGS").functionABC();
        return "CCC";
    }

    @Override
    public String toString() {
        return "TestA";
    }
}
