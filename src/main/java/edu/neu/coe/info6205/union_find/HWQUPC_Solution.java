package edu.neu.coe.info6205.union_find;

import java.util.Arrays;

public class HWQUPC_Solution {
    public static void main(String[] args) {
        //if (args.length == 0)
        //    throw new RuntimeException("Syntax: RandomWalk steps [experiments]");
        for(int n = 16; n <= 262144; n=n*2) {
            int i = 0;
            int nPairs[] = new int[10];
            while (i < 10) {
                //final int nConn = count(Integer.parseInt(args[0]));
                final int nConn = count(n);
                //System.out.println("Number of random connections generated to connect all elements: " + nConn);
                nPairs[i] = nConn;
                i++;
            }
            System.out.println("For N: " + n + " Average: " + Arrays.stream(nPairs).average());
        }
    }

    public static int count(int n) {
        UF h = new UF_HWQUPC(n, true);
        int loopCtr = 0;
        int num1, num2;
        while(h.components() > 1) {
            num1 = (int) (Math.random() * n);
            num2 = (int) (Math.random() * n);
            if (!h.isConnected(num1, num2)) {
                h.union(num1, num2);
            }
            loopCtr++;
        }
        return loopCtr;
    }
}
