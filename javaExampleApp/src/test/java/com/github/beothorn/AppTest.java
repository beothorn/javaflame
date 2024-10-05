package com.github.beothorn;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    public void foo() throws InterruptedException {
        App app = new App();
        app.toString();
        Assertions.assertEquals(1,1);
    }

}