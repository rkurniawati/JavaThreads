import java.util.stream.IntStream;

/**
 * 
 * To specify the number of thread in the ForkJoinPool, specify the java.util.concurrent.ForkJoinPool.common.parallelism system property. For example:
 *
 *    java -Djava.util.concurrent.ForkJoinPool.common.parallelism=100 ParallelLoopEqualChunks
 */
public class ParallelStream {
    static final int REPS = 16;

    public static void main(String[] args) {
        
        // check and parse argument
        int numReps = REPS;
        if (args.length >= 1) {
            numReps = Integer.parseInt(args[0]);
        }

        IntStream.range(0, numReps).parallel().forEach(
            i -> System.out.println("Thread "+Thread.currentThread().getName()+ " performed iteration "+i)
        );

        System.out.println("Done.");

    }
}
