package com.chen.string;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class TestRandom {

    @Test
    public void testMathRandom(){
        int max = 200;
        int min = 100;
        double random = Math.random()*(max - min);
        System.out.println(random);
    }

    @Test
    public void testThreadLocalRandom(){
        //int max = 200;
        //int min = 100;
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        int random = tlr.nextInt(100,200);
        System.out.println(random);
    }

    @Test
    public void randomID(){
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        int random = tlr.nextInt(1000,8000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        String nextId = "tag_test_" + dateStr + String.format("%d", random);
        System.out.println(nextId);
    }
}
