package com.chen.string;

import org.junit.Test;

public class TestStringBuilder {
    /**
     * 测试StringBuilder的append方法
     */
    @Test
    public void testAppend(){
        StringBuilder sb = new StringBuilder("Programming Languages:");
        sb.append("java").append("cpp").append("php").append("c#").append("objective-c");
        System.out.println(sb.toString());
    }
    /**
     * 测试StringBuilder的insert方法
     */
    @Test
    public void testInsert(){
        StringBuilder sb = new StringBuilder("javacppc#objective-c");
        sb.insert(9, "php");
        System.out.println(sb);
    }
    /**
     * 测试StringBuilder的delete方法
     */
    @Test
    public void testDelete(){
        StringBuilder sb = new StringBuilder("javaoraclecppc#php");
        sb.delete(4, 10);//从0开始计数，o是第4个，e是第9个，前包括后不包括
        System.out.println(sb);
    }
}
