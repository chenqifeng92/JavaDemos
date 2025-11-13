package com.chen.algorithm;
import java.util.Scanner;

public class LeftmostOneFinder {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input matrix size
        System.out.print("Enter number of rows (m): ");
        int m = sc.nextInt();
        System.out.print("Enter number of columns (n): ");
        int n = sc.nextInt();

        int[][] matrix = new int[m][n];
        System.out.println("Enter the matrix (only 0s and 1s):");

        // Read the matrix from input
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }

        // Find the leftmost column that contains a 1
        int result = findLeftmostOne(matrix);

        // Print result
        if (result == -1) {
            System.out.println("No 1 found in the matrix.");
        } else {
            System.out.println("The leftmost column containing 1 is at index: " + result);
        }

        sc.close();
    }

    /**
     * Finds the leftmost column that contains at least one '1'.
     *
     * Algorithm:
     * - Start from the top-right corner (row = 0, col = n - 1)
     * - If current value is 1 → move left (col--)
     * - If current value is 0 → move down (row++)
     * - Continue until out of bounds
     *
     * Time Complexity: O(m + n)
     * Space Complexity: O(1)
     */
    public static int findLeftmostOne(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return -1;

        int m = matrix.length;
        int n = matrix[0].length;

        int row = 0;
        int col = n - 1;
        int result = -1;

        // Start from the top-right corner and move accordingly
        while (row < m && col >= 0) {
            if (matrix[row][col] == 1) {
                result = col; // Update leftmost found so far
                col--;        // Move left
            } else {
                row++;        // Move down
            }
        }

        return result;
    }
}
