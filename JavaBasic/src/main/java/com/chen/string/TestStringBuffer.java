package com.chen.string;

public class TestStringBuffer {
    public static void main(String[] args) {
        StringBuffer strbuf = new StringBuffer("abcd");
        StringBuffer strbuf2 = new StringBuffer("Man always remember love b.");
        System.out.println(strbuf.length());
        System.out.println(strbuf.capacity());

        System.out.println(strbuf2.length());
        System.out.println(strbuf2.capacity());
    }
}
