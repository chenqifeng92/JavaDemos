package com.chen.string;

import org.junit.Test;

public class TestRegex {
    /**
     * 使用正则表达式测试email是否合法
     * matches(regex)
     */
    @Test
    public void email(){
        String emailRegex = "^[a-zA-Z0-9_\\.-]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$";
        String email = "messi@huawei.com";
        System.out.println(email.matches(emailRegex));
    }
    /**
     * 使用replaceAll方法实现字符串替换
     * replaceAll(regex, replacement)
     */
    @Test
    public void testReplaceAll(){
        //将str中所有数字替换为*
        String str = "abc123bcd45ef6g7890";
        str = str.replaceAll("\\d+", "*");
        System.out.println(str);
    }
}
