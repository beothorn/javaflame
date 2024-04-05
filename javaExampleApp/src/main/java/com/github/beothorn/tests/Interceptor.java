package com.github.beothorn.tests;

import java.util.ArrayList;
import java.util.List;

public class Interceptor {

    static List<Object> objs = new ArrayList<>();

    public static void interceptConstructor(Object obj){
        System.out.println("<<<<");
        objs.add(obj);
        System.out.println(objs.size());
        objs.forEach(System.out::println);
        System.out.println(">>>>");
    }

}
