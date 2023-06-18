package com.ray.oversolddemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class OversoldDemoApplicationTests {

    @Test
    void contextLoads() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 100, 50, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100)
        );
    }

}
