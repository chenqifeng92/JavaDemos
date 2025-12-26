package com.chen.algorithm.easy;

import org.junit.jupiter.api.Test;

import java.util.*;

public class CommonElements {

    @Test
    public void test1() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {2, 3, 4};

        int[] commonElements = findCommonElements(array1, array2);
        int count = commonElements.length;

        System.out.println(count + " " + Arrays.toString(commonElements));
    }

    @Test
    public void test2() {
        int[] array1 = {1, 2, 3, 3};
        int[] array2 = {2, 3, 4};

        int[] commonElements = findCommonElements(array1, array2);
        int count = commonElements.length;

        System.out.println(count + " " + Arrays.toString(commonElements));
    }

    @Test
    public void test3() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {2, 3, 4, 3};

        int[] commonElements = findCommonElements(array1, array2);
        int count = commonElements.length;

        System.out.println(count + " " + Arrays.toString(commonElements));
    }

    @Test
    public void test4() {
        int[] array1 = {1, 2, 3, 3};
        int[] array2 = {2, 3, 4, 3};

        int[] commonElements = findCommonElements(array1, array2);
        int count = commonElements.length;

        System.out.println(count + " " + Arrays.toString(commonElements));
    }


    public static int[] findCommonElements(int[] array1, int[] array2) {
        Map<Integer, Integer> map1 = new HashMap<>();
        Map<Integer, Integer> map2 = new HashMap<>();

        for (int i = 0; i < array1.length; i++) {
            int count = map1.getOrDefault(array1[i], 0);
            map1.put(array1[i], count + 1);
        }

        for (int i = 0; i < array2.length; i++) {
            int count = map2.getOrDefault(array2[i], 0);
            map2.put(array2[i], count + 1);
        }

        List<Integer> commonElements = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : map1.entrySet()) {
            int num = entry.getKey();
            int count1 = entry.getValue();
            int count2 = map2.getOrDefault(num, 0);

            int minCount = Math.min(count1, count2);

            for (int i = 0; i < minCount; i++) {
                commonElements.add(num);
            }
        }

        int[] result = new int[commonElements.size()];
        int index = 0;

        for (int element : commonElements) {
            result[index++] = element;
        }

        return result;
    }

}
