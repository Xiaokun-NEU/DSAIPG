package com.phasmidsoftware.dsaipg.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ParSort2 {
    public static int cutoff = 1000;  // Default cutoff value
    public static int MAX_DEPTH = (int) (Math.log(Runtime.getRuntime().availableProcessors()) / Math.log(2)); // Default depth limit

    // Main sort method with recursion depth control
    public static void sort(int[] array, int from, int to) {
        sort(array, from, to, 0);  // Start sorting with depth 0
    }

    private static void sort(int[] array, int from, int to, int depth) {
        int size = to - from;

        // If size is below cutoff OR max recursion depth is reached, use sequential sort
        if (size <= cutoff || depth >= MAX_DEPTH) {
            Arrays.sort(array, from, to);
            return;
        }

        int mid = from + size / 2;

        // Increase depth and run left and right partitions in parallel
        CompletableFuture<Void> leftTask = CompletableFuture.runAsync(() -> sort(array, from, mid, depth + 1));
        CompletableFuture<Void> rightTask = CompletableFuture.runAsync(() -> sort(array, mid, to, depth + 1));

        // Wait for both sorting tasks to complete
        CompletableFuture.allOf(leftTask, rightTask).join();

        // Merge sorted partitions
        merge(array, from, mid, to);
    }

    // Merge function to merge two sorted subarrays
    private static void merge(int[] array, int from, int mid, int to) {
        int[] left = Arrays.copyOfRange(array, from, mid);
        int[] right = Arrays.copyOfRange(array, mid, to);
        int i = 0, j = 0, k = from;

        while (i < left.length && j < right.length) {
            array[k++] = (left[i] <= right[j]) ? left[i++] : right[j++];
        }

        while (i < left.length) array[k++] = left[i++];
        while (j < right.length) array[k++] = right[j++];
    }
}
