package com.chen.algorithm.easy;

import java.util.Arrays;

public class TwoSum {
    public static int[] twoSum(int[] nums, int target){
        int[] rs = new int[2];
        for(int i=0;i<nums.length;i++){
            for(int j=0;j<i;j++){
                if(target == nums[i]+nums[j]){
                    rs[0]=j;
                    rs[1]=i;
                }
            }
        }
        return rs;
    }

    public static void main(String[] args) {

        int[] nums = {3,2,4,1};
        int target = 6;
        int rs[] = twoSum(nums,target);
        System.out.println(Arrays.toString(rs));

    }

}
