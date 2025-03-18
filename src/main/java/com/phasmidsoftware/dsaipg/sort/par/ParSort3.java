package com.phasmidsoftware.dsaipg.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ParSort3 {
    public static int cutoff = 1000;
    public static int MAX_DEPTH = 3;

    public static void sort(int[] array, int from, int to) {
        sort(array, from, to, 0);
    }

    private static void sort(int[] array, int from, int to, int depth) {
        int size = to - from;

        if (size <= cutoff || depth >= MAX_DEPTH) {
            Arrays.sort(array, from, to);
            return;
        }

        int mid = from + size / 2;

        CompletableFuture<Void> leftTask = CompletableFuture.runAsync(() -> sort(array, from, mid, depth + 1));
        CompletableFuture<Void> rightTask = CompletableFuture.runAsync(() -> sort(array, mid, to, depth + 1));

        CompletableFuture.allOf(leftTask, rightTask).join();

        merge(array, from, mid, to);
    }

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