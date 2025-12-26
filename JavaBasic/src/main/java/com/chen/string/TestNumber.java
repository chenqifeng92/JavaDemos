package com.chen.string;

import org.junit.jupiter.api.Test;

public class TestNumber {
    /**
     * 测试Number的intValue方法和doubleValue方法
     */
    @Test
    public void testIntValueAndDoubleValue(){
        Number d = 123.45;
        Number n = 123;

        //输出d和n对象所属的类型
        System.out.println(d.getClass().getName());//java.lang.Double
        System.out.println(n.getClass().getName());//java.lang.Integer

        int intValue = d.intValue();
        double doubleValue = d.doubleValue();
        System.out.println(intValue+","+doubleValue);//123,123.45

        intValue = n.intValue();
        doubleValue = n.doubleValue();
        System.out.println(intValue+","+doubleValue);//123,123.0
    }
}
