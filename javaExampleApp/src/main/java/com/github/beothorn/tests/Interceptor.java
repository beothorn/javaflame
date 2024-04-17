package com.github.beothorn.tests;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static void interceptMethod(
        Object self,
        Method method,
        Object[] allArguments,
        Object returnValueFromMethod
    ){
        System.out.println(">>>>");
        System.out.println("self: "+self);
        System.out.println("method: "+method.getName());
        System.out.println("allArguments: "+ Arrays.toString(allArguments));
        System.out.println("returnValueFromMethod: "+returnValueFromMethod);
        System.out.println("<<<<");
    }

}
