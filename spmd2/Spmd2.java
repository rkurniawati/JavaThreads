import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class Spmd2 {

    public static void main(String[] args) {
        // check and parse argument
        if (args.length == 0) {
            System.out.println("Usage " + Spmd2.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        int numThreads = Integer.parseInt(args[0]);
        if (numThreads < 1) {
            System.out.println("Usage " + Spmd2.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        // launch the threads
        executeCode(() -> {
                String threadName = Thread.currentThread().getName();
                Thread.yield();
                String message = "Hello from " +  threadName + " from a pool of " + numThreads;
                System.out.println(message);
           }, numThreads);

        shutdown();
        System.out.println("Done.");
    }

    private static ThreadPoolExecutor tpe;

    private static void executeCode(Runnable r, int numThreads) {
        tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        tpe.prestartAllCoreThreads();
        IntStream.range(0, numThreads).forEach(( i ) -> tpe.execute(r));
    }

    private static void shutdown() {
        try {
            tpe.awaitTermination(100, TimeUnit.MILLISECONDS);            
        } catch (Exception e) {}
        tpe.shutdown();
    }
}