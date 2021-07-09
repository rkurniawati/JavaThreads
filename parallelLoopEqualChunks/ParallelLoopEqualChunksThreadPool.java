import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

/**
 *
 * To specify the number of thread in the ForkJoinPool, specify the java.util.concurrent.ForkJoinPool.common.parallelism system property. For example:
 *
 *    java -Djava.util.concurrent.ForkJoinPool.common.parallelism=100 ParallelLoopEqualChunks
 */
public class ParallelLoopEqualChunksThreadPool {
    static final int REPS = 16;

    static class ChunkExecutor {
        private int startIndex, endIndex;

        public ChunkExecutor(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex; // not inclusive
        }

        public void executeChunk() {
            for(int i = startIndex; i < endIndex; i++) {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {}
                System.out.println("Thread " + Thread.currentThread().getName() + " performed iteration " + i);
            }
        }
    }

    static class RunnableExecutor implements Runnable {
        private ChunkExecutor executor;

        public RunnableExecutor(ChunkExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void run() {
            executor.executeChunk();
        }
    }


    public static void main(String[] args) {

        // check and parse argument
        int numReps = REPS;
        if (args.length >= 1) {
            numReps = Integer.parseInt(args[0]);
        }

        int numProcessors = Runtime.getRuntime().availableProcessors();
        // initialize the thread pool
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(numProcessors);
        tpe.prestartAllCoreThreads();
        System.out.println("Number of parallel threads " + tpe.getCorePoolSize());

        // divide the work
        int numThreads = numProcessors;
        int startIndex = 0;
        for(int i = 0; i < numThreads; i++) {
            int leftOver = (numReps % numThreads <= i) ? 0 : 1; // if REPS is not divisible evenly, spread it over threads
            int chunkSize = numReps / numThreads + leftOver;

            assert(startIndex+leftOver+chunkSize <= numReps);

            tpe.execute(new RunnableExecutor(new ChunkExecutor(startIndex, startIndex + chunkSize)));
            startIndex += chunkSize;
        }

        tpe.shutdown();
        System.out.println("Done.");

    }

}
