package com.github.beothorn;

public class AnotherClass {

    private final String value;

    public AnotherClass(String value){

        this.value = value;
    }

    public String getValue() {
        int j = 0;
        for (int i = 0; i < 100000000; i++) {
            j++;
        }
        return value + j;
    }
}
