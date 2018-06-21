package com.chen.string;

import org.junit.Test;

public class TestString {
    /**
     * 测试String常量池
     */
    @Test
    public void testConstantPool(){
        String str1 = "Hello";
        //不会创建新的对象，而是使用常量池中已有的"Hello".
        String str2 = "Hello";
        System.out.println(str1==str2);

        //使用new关键字会创建新的String对象.
        String str3 = new String("Hello");
        System.out.println(str1==str3);
    }
    /**
     * 获取String对象的长度
     * length()
     */
    @Test
    public void testLength(){
        String str1 = "Hello";
        System.out.println(str1.length());

        /*
         * 在内存中采用Unicode编码，每个字符占用两个字节，
         * 任何一个字符（无论中文还是英文）都算1个字符长度
         */
        String str2 = "你好，String";
        System.out.println(str2.length());
    }
    /**
     * 在一个字符串中检索另外一个字符串
     * indexOf()
     */
    @Test
    public void testIndexOf(){
        String str = "I can because I think I can.";
        //检索字符串"can"在此字符串中第一次出现的索引位置（从0开始计数）
        int index = str.indexOf("can");
        System.out.println(index);//2
        //检索字符串"can"在此字符串中最后一次出现的索引位置
        index = str.lastIndexOf("can");
        System.out.println(index);//24
        //从索引位置6开始，检索字符串"can"在此字符串第一次出现的索引位置
        index = str.indexOf("can",6);
        System.out.println(index);//24
        //检索字符串"Can"在此字符串中第一次出现的索引位置，注意"Can"为大写
        index = str.indexOf("Can");
        System.out.println(index);//-1,找不到则返回-1
    }
    /**
     * 在一个字符串中截取指定字符串
     * substring()
     */
    @Test
    public void testSubString(){
        String str = "http://www.oracle.com";
        String subStr = str.substring(11,17);//截取索引11-17的字符串
        System.out.println(subStr);//oracle

        subStr = str.substring(7);//截取索引7之后的字符串
        System.out.println(subStr);//www.oracle.com
    }
    /**
     * 去掉一个字符串的前导和后继字符
     * trim()
     */
    @Test
    public void testTrim(){
        String userName = "  good man  ";
        System.out.println(userName);
        System.out.println(userName.length());

        userName = userName.trim();
        System.out.println(userName);
        System.out.println(userName.length());
    }
    /**
     * 遍历一个字符串中的字符序列
     * charAt()
     */
    @Test
    public void testCharAt(){
        String name = "Whatisjava?";
        for(int i=0;i<name.length();i++){
            char c = name.charAt(i);
            System.out.println(c+" ");
        }
    }
    /**
     * 检查一个字符串是否以指定字符开头或结尾
     * startsWith()
     * endsWith()
     */
    @Test
    public void testStartsWithAndEndsWith(){
        String str = "Thinking in Java";
        System.out.println(str.endsWith("Java"));
        System.out.println(str.startsWith("T"));
        System.out.println(str.startsWith("thinking"));
    }
    /**
     * 转换字符串中的中英文大小写形式
     * toUpperCase()
     * toLowerCase()
     */
    @Test
    public void TestToUpperCaseAndToLowerCase(){
        String str = "我喜欢Java";

        str = str.toUpperCase();
        System.out.println(str);
        str = str.toLowerCase();
        System.out.println(str);
    }
    /**
     * 将其他字符串类型转换为字符串类型
     */
    @Test
    public void testValueOf(){
        double pi = 3.1415926;
        int value = 123;
        boolean flag = true;
        char[] charArr = {'a','b','c','d','e','f','g'};

        String str = String.valueOf(pi);
        System.out.println(str);
        str = String.valueOf(value);
        System.out.println(str);
        str = String.valueOf(flag);
        System.out.println(str);
        str = String.valueOf(charArr);
        System.out.println(str);
    }
}
