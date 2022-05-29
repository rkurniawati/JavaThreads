import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class Spmd {

    private static String threadName;
    private static int numThreads;

    public static void main(String[] args) throws Exception {

        // check and parse argument
        if (args.length == 0) {
            System.out.println("Usage SpmdStream numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        numThreads = Integer.parseInt(args[0]);
        if (numThreads < 1) {
            System.out.println("Usage SpmdStream numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        // launch the parallel stream using the custom pool
        ForkJoinPool customPool = new ForkJoinPool(numThreads);
        customPool.submit(() -> 
            IntStream.range(0, numThreads).parallel().forEach(i -> {
                threadName = Thread.currentThread().getName();
                Thread.yield();
                String message = "Hello from " +  threadName + " from a pool of " + numThreads;
                System.out.println(message);
            })
        ).get();

        System.out.println("Done.");
    }
}