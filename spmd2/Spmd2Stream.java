import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class Spmd2Stream {


    public static void main(String[] args) throws Exception {

        // check and parse argument
        if (args.length == 0) {
            System.out.println("Usage Spmd2Stream numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        int poolNumThreads = Integer.parseInt(args[0]);
        if (poolNumThreads < 1) {
            System.out.println("Usage Spmd2Stream numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        // launch the parallel stream using the custom pool
        ForkJoinPool customPool = new ForkJoinPool(poolNumThreads);
        customPool.submit(() -> 
            IntStream.range(0, poolNumThreads).parallel().forEach(i -> {
                String threadName = Thread.currentThread().getName();
                int numThreads = customPool.getParallelism();
                Thread.yield();
                String message = i + ": Hello from " +  threadName + " from a pool of " + numThreads;
                System.out.println(message);
            })
        ).get();

        System.out.println("Done.");
    }
}