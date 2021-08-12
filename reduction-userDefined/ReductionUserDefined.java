/* Reduction2.java computes a table of factorial values,
 *  using Java's BigInteger class to explore
 *
 *  Adapted from original Joel Adams' patternlet
 *
 *  Usage: 
 *
 *  Exercise:
 *  - Build and run, record sequential time in a spreadsheet
 *  - Uncomment #pragma omp parallel for directive, rebuild,
 *     and read the error message carefully.
 *  - Uncomment the #pragma omp declare directive, rebuild,
 *     and note the user-defined * reduction for a BigInt.
 *  - Rerun, using 2, 4, 6, 8, ... threads, recording
 *     the times in the spreadsheet.
 *  - Create a chart that plots the times vs the # of threads.
 *  - Experiment with different n values
 */

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class ReductionUserDefined {
    static BigInteger factorial(int numThreads, int n) {
        // you can also use this:
        // return IntStream.range(1, n).parallel().mapToObj(i -> BigInteger.valueOf((long)i))
        //        .reduce( BigInteger.valueOf(1l), BigInteger.multiply);

        ForkJoinPool fjp = new ForkJoinPool(numThreads);
        try {
            return fjp.submit(() ->
             IntStream.range(1, n).parallel().mapToObj(i -> BigInteger.valueOf((long)i))
                    .reduce( BigInteger.valueOf(1l), (accumulator, current)-> {
                        //System.out.println("Thread: " + Thread.currentThread().getName());
                        return accumulator.multiply(current);
                    })).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // check and parse argument
        int numThreads = Runtime.getRuntime().availableProcessors();
        if (args.length < 1) {
            System.out.println("Usage " + ReductionUserDefined.class.getName() + " numThreads n.");
            System.out.println("Using default number of Threads " + numThreads);
        } else {
            numThreads = Integer.parseInt(args[0]);
        }

        int n = 4096;
        if (args.length == 2) {
            n = Integer.parseInt(args[1]);
        }

        long startTime = System.currentTimeMillis();
        BigInteger result = factorial(numThreads, n);
        long duration = System.currentTimeMillis() - startTime;

        System.out.println("Result = " + result.toString(10));
        System.out.println("Time = " + duration + " ms");
    }
}
