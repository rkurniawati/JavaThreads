import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * 
 * To specify the number of thread in the ForkJoinPool, 
 * specify the java.util.concurrent.ForkJoinPool.common.parallelism 
 * system property. For example:
 *
 *    java -Djava.util.concurrent.ForkJoinPool.common.parallelism=100 ParallelLoopEqualChunks
 */
public class ParallelLoopChunksOf1CustomPool {
    static final int REPS = 16;
    static int numReps = REPS;

    public static void main(String[] args) throws Exception {
        
        // check and parse argument
        if (args.length >= 1) {
            numReps = Integer.parseInt(args[0]);
        }

        // initialize the thread pool
        ForkJoinPool fjp = new ForkJoinPool();
        int numThreads = fjp.getParallelism();
        System.out.println("Number of threads " + numThreads);
        fjp.submit(() -> 
            IntStream.range(0, numReps)
            .parallel()
            .forEach(i -> System.out.println("Thread " + 
                Thread.currentThread().getName() + " performed iteration " + i))).get();
        System.out.println("Done.");

    }
}
