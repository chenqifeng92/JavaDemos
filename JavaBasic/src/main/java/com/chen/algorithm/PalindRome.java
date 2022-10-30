package com.chen.algorithm;

import java.util.Arrays;

public class PalindRome {
    public static boolean isPalindrome(int x) {
        String xtr = String.valueOf(x);
        String rtx = new StringBuffer(xtr).reverse().toString();
        System.out.println("rtx="+rtx);
        if (rtx.equals(xtr)){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
        int x = -12121;
        System.out.println(isPalindrome(x));
    }
}
