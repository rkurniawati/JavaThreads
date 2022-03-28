import java.util.stream.IntStream;
import java.util.concurrent.ForkJoinPool;

public class ParallelLoopEqualChunksParallelStreamCustomPool {
    static final int REPS = 16;
    static int numReps = REPS;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage ParallelLoopEqualChunks numberOfRepetitions numberOfThreads");
        }

        // check and parse arguments
        final int numReps = (args.length > 0) ? Integer.parseInt(args[0]) : 16;
        final int numThreads = (args.length > 1) ? Integer.parseInt(args[0]) : Runtime.getRuntime().availableProcessors();
        
        System.out.println("Number of threads " + numThreads);

        ForkJoinPool customPool = new ForkJoinPool(numThreads);
        customPool.submit(() -> 
            IntStream.range(0, numReps)
                    .parallel()
                    .forEach(i -> {
                        sleepALittle(5);
                        System.out.println("Thread " + Thread.currentThread().getName() + " completed iteration " + i);
                    })
        ).get();
        System.out.println("Done.");

    }
}
