package com.chen.string;

import org.junit.Test;

public class TestInteger {
    /**
     * 测试Integer中的parseInt方法
     */
    @Test
    public void testParseInt(){
        String str = "123";
        int value = Integer.parseInt(str);
        System.out.println(value);//123
    }
    @Test
    public void testParseDouble(){
        String str = "12345.00";
        double value = Double.parseDouble(str);
        System.out.println(value);

        //str = "$12345.00";
        //value = Double.parseDouble(str); //会抛出NumberFormatException
    }
}
