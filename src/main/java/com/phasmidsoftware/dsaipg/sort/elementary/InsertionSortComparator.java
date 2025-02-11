/*
 * Copyright (c) 2024. Robin Hillyard
 */
package com.phasmidsoftware.dsaipg.sort.elementary;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import com.phasmidsoftware.dsaipg.sort.Helper;
import static com.phasmidsoftware.dsaipg.sort.InstrumentedComparatorHelper.getRunsConfig;
import com.phasmidsoftware.dsaipg.sort.Sort;
import com.phasmidsoftware.dsaipg.sort.SortWithHelper;
import com.phasmidsoftware.dsaipg.util.Config;
import com.phasmidsoftware.dsaipg.util.Config_Benchmark;
/**
 * A class for performing insertion sort using a comparator, extending functionality from SortWithHelper.
 * This includes methods for initialization and invocation of insertion sort,
 * along with specific utilities like counting inversions.
 *
 * @param <X> the type of elements to be sorted, which can be compared using a provided comparator.
 */
public class InsertionSortComparator<X> extends SortWithHelper<X> {
    /**
     * Constructor for InsertionSortComparator, which initializes the comparator with the provided helper.
     *
     * @param helper the Helper object to be used for managing the sorting process.
     */
    public InsertionSortComparator(Helper<X> helper) {
        super(helper);
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 4000, 8000, 16000}; // Doubling method

        benchmarkRandomOrder(sizes);
        benchmarkOrdered(sizes);
        benchmarkPartiallyOrdered(sizes);
        benchmarkReverseOrdered(sizes);
    }

    /**
     * Benchmark for Randomly Ordered Arrays
     */
    private static void benchmarkRandomOrder(int[] sizes) {
        System.out.println("\n===== Random =====");
        System.out.printf("%-15s %-15s\n", "n", "Random (ms)");

        for (int n : sizes) {
            Integer[] array = generateArray(n, "random");
            double time = benchmarkSort(array);
            System.out.printf("%-15d %-15.3f\n", n, time);
        }
    }

    /**
     * Benchmark for Ordered (Best Case) Arrays
     */
    private static void benchmarkOrdered(int[] sizes) {
        System.out.println("\n===== Ordered =====");
        System.out.printf("%-15s %-15s\n", "n", "Ordered (ms)");

        for (int n : sizes) {
            Integer[] array = generateArray(n, "ordered");
            double time = benchmarkSort(array);
            System.out.printf("%-15d %-15.3f\n", n, time);
        }
    }

    /**
     * Benchmark for Partially Ordered Arrays
     */
    private static void benchmarkPartiallyOrdered(int[] sizes) {
        System.out.println("\n===== Partially Ordered =====");
        System.out.printf("%-15s %-25s\n", "n", "Partially-ordered (ms)");

        for (int n : sizes) {
            Integer[] array = generateArray(n, "partially-ordered");
            double time = benchmarkSort(array);
            System.out.printf("%-15d %-25.3f\n", n, time);
        }
    }

    /**
     * Benchmark for Reverse Ordered (Worst Case) Arrays
     */
    private static void benchmarkReverseOrdered(int[] sizes) {
        System.out.println("\n===== Reverse Ordered =====");
        System.out.printf("%-15s %-25s\n", "n", "Reverse-ordered (ms)");

        for (int n : sizes) {
            Integer[] array = generateArray(n, "reverse");
            double time = benchmarkSort(array);
            System.out.printf("%-15d %-25.3f\n", n, time);
        }
    }

    /**
     * Runs insertion sort on an array and measures execution time.
     */
    private static double benchmarkSort(Integer[] array) {
        Integer[] copy = Arrays.copyOf(array, array.length);
        long start = System.nanoTime();
        insertionSort(copy);
        long end = System.nanoTime();
        return (end - start) / 1_000_000.0; // Convert nanoseconds to milliseconds
    }

    /**
     * Implements Insertion Sort.
     */
    public static void insertionSort(Integer[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j]; // Shift elements to the right
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * Generates an array of specified order type.
     */
    private static Integer[] generateArray(int n, String type) {
        Integer[] array = new Integer[n];
        Random random = new Random();

        switch (type) {
            case "random":
                for (int i = 0; i < n; i++) array[i] = random.nextInt(n);
                break;
            case "ordered":
                for (int i = 0; i < n; i++) array[i] = i;
                break;
            case "partially-ordered":
                for (int i = 0; i < n; i++) array[i] = (i % 5 == 0) ? random.nextInt(n) : i;
                break;
            case "reverse":
                for (int i = 0; i < n; i++) array[i] = n - i;
                break;
        }
        return array;
    }


    /**
     * Constructor for any subclasses to use.
     *
     * @param description the description.
     * @param comparator  the comparator to use.
     * @param N           the number of elements expected.
     * @param nRuns       the number of runs to be expected (this is only significant when instrumenting).
     * @param config      the configuration.
     */
    protected InsertionSortComparator(String description, Comparator<X> comparator, int N, int nRuns, Config config) {
        super(description, comparator, N, nRuns, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param nRuns  the number of runs to be expected (this is only significant when instrumenting).
     * @param config the configuration.
     */
    public InsertionSortComparator(Comparator<X> comparator, int N, int nRuns, Config config) {
        this(DESCRIPTION, comparator, N, nRuns, config);
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();

        // TO BE IMPLEMENTED 
        for (int i = from + 1; i < to; i++) { //from + 1, because first one is ordered
            int j = i;
            while (j > from && helper.swapStableConditional(xs, j)) { 
                j--; // Shift element left 
            }
        }
    }

    public static final String DESCRIPTION = "Insertion sort";

    /**
     * Sorts the given array in-place using the provided insertion sort comparator.
     *
     * @param <T> the generic type parameter that extends Comparable.
     * @param ts  the array of elements to be sorted, where elements must implement {@code Comparable}.
     *            The method modifies this array directly to produce the sorted order.
     * @throws RuntimeException if an IOException occurs during the sorting process.
     */
    public static <T extends Comparable<T>> void sort(T[] ts) {
        try (InsertionSortComparator<T> sort = new InsertionSortComparator<>(DESCRIPTION, Comparable::compareTo, ts.length, 1, Config.load(InsertionSortComparator.class))) {
            sort.mutatingSort(ts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a case-insensitive string sorter using an insertion sort comparator.
     *
     * @param n      the expected number of elements to be sorted.
     * @param config the configuration object containing necessary settings.
     * @return a {@code SortWithHelper<String>} instance configured for case-insensitive string sorting.
     */
    public static Sort<String> stringSorterCaseInsensitive(int n, Config config) {
        return new InsertionSortComparator<>(DESCRIPTION, String.CASE_INSENSITIVE_ORDER, n, getRunsConfig(config), config);
    }

    /**
     * This method is designed to count inversions in quadratic time, using insertion sort.
     *
     * @param ts  an array of comparable T elements.
     * @param <T> the underlying type of the elements.
     * @return the number of inversions in ts, which remains unchanged.
     */
    public static <T> long countInversions(T[] ts, Comparator<T> comparator) {
        final Config config = Config_Benchmark.setupConfigFixes();
        try (InsertionSortComparator<T> sorter = new InsertionSortComparator<>(comparator, ts.length, getRunsConfig(config), config)) {
            Helper<T> helper = sorter.getHelper();
            sorter.sort(ts, true);
            return helper.getFixes();
        }
    }

}