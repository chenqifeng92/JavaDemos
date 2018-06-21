package com.chen.basic;

import java.util.Arrays;

public class TestSort {
    public static void main(String[] args){
        int[] scores = {89,-23,64,91,119,52,73};
        scoreSortByArray(scores);
    }


    /**
     * sort by foreach array
     * @param scores
     */
    public static void scoreSortByArray(int[] scores){
        Arrays.sort(scores);
        int count = 0;
        int[] resultScores = new int[3];
        for (int i=scores.length-1;i>=0;i--){
            if (scores[i]<0 || scores[i]>=100)
                continue;
            System.out.println(scores[i]);
            count++;
            if (count == 3)
                break;
        }
    }
}
