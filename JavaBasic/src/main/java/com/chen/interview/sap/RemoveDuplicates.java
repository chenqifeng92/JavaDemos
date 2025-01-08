package com.chen.interview.sap;

/**
 * Given a sorted integer array, remove the duplicates in place such that each element appear only once and keep the order, then output the result. Do not allocate extra space for another array, you must do this by modifying the input array in-place.
 * IMPORTANT!!!!!!
 * Map Set List Stream  and something like that are not allowed for this solution.
 * For example:
 * Input:   [1,3,4,5,5,5,6,6,7,7]
 * Output:
 * 1
 * 3
 * 4
 * 5
 * 6
 * 7
 *
 * Your code may look something like this:
 *
 * int [] testData = new int[]{1,3,4,5,5,5,6,6,7,7};
 *
 *
 * // remove the duplicates !!! Can't OUTPUT in this PART
 * // One possible solution is to cover the current element with the next one,  and do this one by one.
 * // ……
 * // Some code
 * // ……
 *
 * // output HERE!!
 * // IMPORTANT: You can reduce the length, but CAN NOT FILTER the array.
 * // ……
 * // Some code
 * // ……
 */
public class RemoveDuplicates {
    public static void main(String[] args) {
        int [] testData = new int[]{1,3,4,5,5,5,6,6,7,7};

        int count = removeDuplicates(testData);

        for(int i=0;i<count;i++){
            System.out.println(testData[i]);
        }
    }

    public static int removeDuplicates(int[] nums) {
        int index = 0;
        for (int i = 1;i< nums.length;i++){
            if(nums[i] != nums[index]){
                index++;
                nums[index] = nums[i];
            }
        }
        return index+1;
    }
}
