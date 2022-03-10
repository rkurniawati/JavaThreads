import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

/**
 * 
 * To specify the number of thread in the ForkJoinPool, specify the 
 * java.util.concurrent.ForkJoinPool.common.parallelism system property. 
 * 
 * For example:
 *
 *    java -Djava.util.concurrent.ForkJoinPool.common.parallelism=100 ParallelLoopEqualChunks
 */
public class ParallelLoopEqualChunks {
    static final int REPS = 16;

    static class ChunkExecutor implements Callable<Void> {
        private int startIndex, endIndex;

        public ChunkExecutor(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex; // not inclusive
        }

        @Override
        public Void call() throws Exception {
            for(int i = startIndex; i < endIndex; i++) {
                System.out.println("Thread " + Thread.currentThread().getName() + " performed iteration " + i);
            }
            return null;
        }
    }

    public static void main(String[] args) {
        
        // check and parse argument
        int numReps = REPS;
        if (args.length >= 1) {
            numReps = Integer.parseInt(args[0]);
        }

        // initialize the thread pool
        ForkJoinPool fjp = new ForkJoinPool();
        int numThreads = fjp.getParallelism();
        System.out.println("Number of repetitions " + numReps);
        System.out.println("Number of parallel threads " + numThreads);

        // divide the work
        List<ChunkExecutor> tasks = new ArrayList<>();
        int startIndex = 0;

        for(int i = 0; i < numThreads; i++) {
            int leftOver = (numReps % numThreads <= i) ? 0 : 1; // if REPS is not divisible evenly, spread it over threads
            int chunkSize = numReps / numThreads + leftOver;

            assert(startIndex+leftOver+chunkSize <= numReps);

            tasks.add(new ChunkExecutor(startIndex, startIndex + chunkSize));
            startIndex += chunkSize;
        }

        fjp.invokeAll(tasks);

        System.out.println("Done.");

    }
}
