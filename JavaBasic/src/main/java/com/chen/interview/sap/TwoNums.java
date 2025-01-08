package com.chen.interview.sap;

import java.util.HashMap;

public class TwoNums {
    public static int[] twoNums(int[] nums,int target){
        //create a HashMap

        HashMap<Integer,Integer> map = new HashMap<>();
        //iterate
        for(int i=0;i<nums.length;i++){
            int complement = target - nums[i];
            //check complement in map or not
            if(map.containsKey(complement)){
                //return the indices of complement and current num
                return new int[]{map.get(complement),i};
            }
            //if not found,store the current num and its index
            map.put(nums[i],i);

        }
        //return an empty array if no solution is found
        return new int[]{};
    }

    public static void main(String[] args){

        int[] nums1 = {5, 2, 3};
        int target1 = 8;
        int[] result1 = twoNums(nums1,target1);
        System.out.println("num1:"+result1[0]+",target1:"+result1[1]);

    }
}
