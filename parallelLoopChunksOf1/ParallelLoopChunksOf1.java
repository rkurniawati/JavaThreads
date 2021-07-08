import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * 
 * To specify the number of thread in the ForkJoinPool, specify the java.util.concurrent.ForkJoinPool.common.parallelism system property. For example:
 *
 *    java -Djava.util.concurrent.ForkJoinPool.common.parallelism=100 ParallelLoopEqualChunks
 */
public class ParallelLoopChunksOf1 {
    static final int REPS = 16;

    static class ChunkExecutor implements Callable<Void> {
        private int startIndex, endIndex, step;

        public ChunkExecutor(int startIndex, int endIndex, int step) {
            this.startIndex = startIndex;
            this.endIndex = endIndex; // not inclusive
            this.step = step;
        }

        @Override
        public Void call() throws Exception {
            for(int i = startIndex; i < endIndex; i+=step) {
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
        int size = fjp.getParallelism();

        // divide the work
        List<ChunkExecutor> tasks = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            tasks.add(new ChunkExecutor(i, REPS, size));
        }

        fjp.invokeAll(tasks);

        System.out.println("Done.");

    }
}
