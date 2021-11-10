package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        //System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());

        int noThreads = 2;
        //for(int nthreads = 2; nthreads <= 2048; nthreads=nthreads*2) {
        for(int nthreads = 16; nthreads <= 32; nthreads=nthreads*2) {
            noThreads = nthreads;
            ParSort.myPool = new ForkJoinPool(nthreads);
            System.out.println("Degree of parallelism: " + ParSort.myPool.getParallelism());
            Random random = new Random();
            for(int arraySz = 1000000; arraySz < 10000000; arraySz*=2) {
                System.out.println("ArraySize " + arraySz);
                int[] array = new int[arraySz];
                ArrayList<Long> timeList = new ArrayList<>();
                for (int j = 100; j < arraySz/100; j+=100) {
                    ParSort.cutoff = 100 * j;
                    // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                    long time;
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 10; t++) {
                        for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                        ParSort.sort(array, 0, array.length);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    timeList.add(time);

                    System.out.println("cutoff," + (ParSort.cutoff) + ",10times Time," + time + ",ms,ArraySize"+ arraySz);

                }
                try {
                    String fileName = "/src/result_" + noThreads + ".csv";
                    FileOutputStream fis = new FileOutputStream(fileName);
                    OutputStreamWriter isr = new OutputStreamWriter(fis);
                    BufferedWriter bw = new BufferedWriter(isr);
                    int j = 0;
                    for (long i : timeList) {
                        String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                        j++;
                        bw.write(content);
                        bw.flush();
                    }
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        //else
        //    // TODO sort this out
        //    if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
        //        ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
