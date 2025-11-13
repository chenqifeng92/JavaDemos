package com.chen.algorithm;

import java.util.*;

public class findPoisonedDuration {

    // Method to calculate the total poisoned duration
    public static int findPoisonedDuration(int[] timeSeries, int duration) {
        if (timeSeries.length == 0) return 0;

        int total = 0;

        // Iterate through attack times except the last one
        for (int i = 0; i < timeSeries.length - 1; i++) {
            int diff = timeSeries[i + 1] - timeSeries[i];
            // If the next attack happens before the poison ends, overlap occurs
            total += Math.min(diff, duration);
        }

        // The last attack always lasts for the full duration
        total += duration;

        return total;
    }

    // Main method to run test cases
    public static void main(String[] args) {
        // Example 1
        int[] timeSeries1 = {1, 4};
        int duration1 = 2;
        int result1 = findPoisonedDuration(timeSeries1, duration1);
        System.out.println("Example 1 result: " + result1);  // Expected output: 4

        // Example 2
        int[] timeSeries2 = {1, 2};
        int duration2 = 2;
        int result2 = findPoisonedDuration(timeSeries2, duration2);
        System.out.println("Example 2 result: " + result2);  // Expected output: 3
    }
}
