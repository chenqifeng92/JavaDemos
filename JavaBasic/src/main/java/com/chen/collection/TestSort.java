package com.chen.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class TestSort {
    /**
     * 测试sort方法
     */
    @Test
    public void testSort(){
        List<Integer> list = new ArrayList<Integer>();
        Random r = new Random(1);//1是随机数生成器种子保证每次生成的随机数相同
        for(int i=0;i<10;i++){
            list.add(r.nextInt(100));
        }
        System.out.println(list);

        Collections.sort(list);
        System.out.println(list);
    }

}
