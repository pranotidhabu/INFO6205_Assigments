package edu.neu.coe.info6205;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.GenericSort;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.elementary.InsertionSort;
import edu.neu.coe.info6205.util.*;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public class BenchmarkDriver {
    private static Config config;
    public static int N;
    final static LazyLogger logger = new LazyLogger(BenchmarkDriver.class);

    public static void main(String []args) {

        System.out.println("Benchmarking insertion sort");
        resetArraySize();
        //Array with elements in random order
        for (int i = 0; i<5; i++) {
            Helper<Integer> helper = new BaseHelper<>("Insertion Sort - Random", N, config);
            Supplier<Integer[]> supplier = () -> helper.random(Integer.class, Random::nextInt);
            BenchmarkDriver.runBenchmark(helper, supplier, "Random");
            N = N * 2;
        }
        resetArraySize();

        //Benchmarking for ordered array
        for (int i = 0; i<5; i++) {
            Helper<Integer> helper = new BaseHelper<>("Insertion Sort - Random", N, config);
            Supplier<Integer[]> supplier = () -> createArray(N, true);
            BenchmarkDriver.runBenchmark(helper, supplier, "Ordered");
            N = N * 2;
        }
        resetArraySize();

        //Benchmarking for reverse-ordered array
        for (int i = 0; i<5; i++) {
            Helper<Integer> helper = new BaseHelper<>("Insertion Sort - Random", N, config);
            Supplier<Integer[]> supplier = () -> createArray(N, false);;
            BenchmarkDriver.runBenchmark(helper, supplier, "Reverse Ordered");
            N = N * 2;
        }
        resetArraySize();

        for (int i=0; i<5; i++) {
            Helper<Integer> helper = new BaseHelper<>("Insertion Sort - Random", N, config);
            Supplier<Integer[]> supplier = () -> createPartiallyOrderedArray(N);;
            BenchmarkDriver.runBenchmark(helper, supplier, "Partially Ordered");
            N = N * 2;
        }
    }

    private static Integer[] createArray(int size, boolean asc) {
        final Integer []orderedArr = new Integer[size];
        for(int i = 1; i<=N; i++){
            if (asc){
                orderedArr[i-1] = i;
            }
            else {
                orderedArr[i-1] = size-i+1;
            }
        }
        return orderedArr;
    }

    private static Integer[] createPartiallyOrderedArray(int size) {
        final Integer []orderedArr = new Integer[size];
        for(int i = 1; i<N/2; i++){
            Random random = new Random();
            orderedArr[i-1] = random.nextInt(N/2) + 1;
        }
        for(int i = N/2; i<=N; i++) {
            orderedArr[i - 1] = i;
        }
        return orderedArr;
    }

    public static void resetArraySize(){
        N = 4000;
    }

    private static void runBenchmark(Helper helper, Supplier supplier, String description){
        final GenericSort<Integer> sort = new InsertionSort<>(helper);
        final Benchmark<Integer[]> benchmark = new Benchmark_Timer<>(
                "Insert sort " + description + " for " + N + " Integers",
                (xs) -> Arrays.copyOf(xs, xs.length),
                sort::mutatingSort,
                null
        );
        logger.info(Utilities.formatDecimal3Places(benchmark.runFromSupplier(supplier, 100)) + " ms");
    }

}


