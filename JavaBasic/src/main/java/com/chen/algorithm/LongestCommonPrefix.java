package com.chen.algorithm;

import java.util.Arrays;

public class LongestCommonPrefix {

    public static String longestCommonPrefix(String[] strs){
        int[] bound = new int[strs.length];
        for(int i=0;i< strs.length;i++){
            bound[i] = strs[i].length();
        }
        int min = Arrays.stream(bound).min().getAsInt();

        String[] rs = new String[strs.length];
        for(int i=0;i<strs.length;i++) {
            rs[i] = strs[i].substring(0,i);
        }
        
        return null;
    }

    public static void main(String[] args) {
        String[] strs = {"flower","flow","flight"};

        longestCommonPrefix(strs);
    }
}
