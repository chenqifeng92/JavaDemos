package com.chen.interview.sap;

import java.util.Arrays;

public class MoveZero {
    public static void moveZero(int[] nums){
        int index = 0;
        //move all non-zero elements to front
        for(int num : nums){
            if(num!=0){
                nums[index++] = num;
            }

        }
        // fill the remain postions with 0
        while(index < nums.length){
            nums[index++] = 0;
        }
    }

    public static void main(String[] args){
        int[] nums = {9,0,6,0,8};
        moveZero(nums);
        System.out.println(Arrays.toString(nums));
    }
}
