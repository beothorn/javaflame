/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.beothorn;

public class App {
    public static int functionThatCallsFunctions(){
        int foo = 0;

        foo = fastFunction(foo);
        Thread thread = new Thread(() -> slowFunction(99));
        thread.start();
        foo = slowFunction(foo);
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return foo;
    }

    private static int slowFunction(int foo) {
        for (int i = 0; i < 10000; i++) {
            foo -= 5000;
            for (int j = 0; j < 200; j++) {
                foo++;
                for (int k = 0; k < 1000; k++) {
                    foo--;
                }
            }
        }
        return foo;
    }

    private static int fastFunction(int foo) {
        for (int i = 0; i < 1000; i++) {
            foo++;
        }
        return foo;
    }

    public static void main(String[] args) {
        System.out.println("Started");
        int i = 0;
        while (true){
            i++;
            AnotherClass foo = new AnotherClass("foo");
            foo.getValue();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
