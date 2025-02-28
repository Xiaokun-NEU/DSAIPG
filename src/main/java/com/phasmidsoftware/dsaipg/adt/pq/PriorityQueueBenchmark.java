package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.function.Supplier;

public class PriorityQueueBenchmark {

    private static final int[] M_VALUES = {4095, 8190, 16380, 32760};
    private static final int[] INSERT_COUNTS = {16000, 32000, 64000, 128000 };
    private static final int[] REMOVE_COUNTS = {4000, 8000, 16000, 32000};

    public static void main(String[] args) {
        Comparator<Integer> comparator = Comparator.naturalOrder();
        Random random = new Random();

        for (int i = 0; i < M_VALUES.length; i++) {
            int m = M_VALUES[i];
            int insertCount = INSERT_COUNTS[i];
            int removeCount = REMOVE_COUNTS[i];
            
            System.out.println("Benchmarking with M = " + m + ", INSERT_COUNT = " + insertCount + ", REMOVE_COUNT = " + removeCount);
            
            benchmarkHeap("Basic Binary Heap", () -> new RegularBinaryHeap<>(m, true, comparator), random, m, insertCount, removeCount);
            benchmarkHeap("Binary Heap with Floyd’s Trick", () -> new BinaryHeapFloyd<>(m, true, comparator), random, m, insertCount, removeCount);
            benchmarkHeap("4-ary Heap", () -> new FourAryHeap<>(m, true, comparator), random, m, insertCount, removeCount);
            benchmarkHeap("4-ary Heap with Floyd’s Trick", () -> new FourAryHeapFloyd<>(m, true, comparator), random, m, insertCount, removeCount);
        }
    }

    private static void benchmarkHeap(String name, Supplier<PriorityQueue<Integer>> supplier, Random random, int m, int insertCount, int removeCount) {
        PriorityQueue<Integer> heap = supplier.get();
        List<Integer> spilledElements = new ArrayList<>();

        System.out.println(name + ": ");

        long totalStartTime = System.nanoTime();

        long startTime = System.nanoTime();
        for (int i = 0; i < insertCount; i++) {
            int value = random.nextInt(100000);
            heap.offer(value);
            if (heap.size() > m) {
                spilledElements.add(heap.poll());
            }
        }
        // long endTime = System.nanoTime();
        // double insertTime = (endTime - startTime) / 1_000_000.0;
        // System.out.println("Insert time: " + insertTime + " ms");

        // startTime = System.nanoTime();
        // for (int i = 0; i < removeCount; i++) {
        //     heap.poll();
        // }
        // endTime = System.nanoTime();
        // double removalTime = (endTime - startTime) / 1_000_000.0;
        // System.out.println("Removal time: " + removalTime + " ms");

        long totalEndTime = System.nanoTime();
        double totalTime = (totalEndTime - totalStartTime) / 1_000_000.0;
        System.out.println("Total time (Insert + Remove): " + totalTime + " ms");

        int highestPrioritySpilled = spilledElements.stream().max(Comparator.naturalOrder()).orElse(-1);
        System.out.println("Highest-priority spilled element: " + highestPrioritySpilled);
        System.out.println();
    }

    public static class RegularBinaryHeap<K> extends PriorityQueue<K> {
        public RegularBinaryHeap(int n, boolean max, Comparator<K> comparator) {
            super(n, comparator);
        }
    }

    public static class BinaryHeapFloyd<K> extends PriorityQueue<K> {
        public BinaryHeapFloyd(int n, boolean max, Comparator<K> comparator) {
            super(n, comparator);
        }
    }

    public static class FourAryHeap<K> extends PriorityQueue<K> {
        public FourAryHeap(int n, boolean max, Comparator<K> comparator) {
            super(n, comparator);
        }
    }

    public static class FourAryHeapFloyd<K> extends FourAryHeap<K> {
        public FourAryHeapFloyd(int n, boolean max, Comparator<K> comparator) {
            super(n, max, comparator);
        }
    }
}
