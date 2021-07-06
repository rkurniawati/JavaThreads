import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class Spmd {

    static String threadName;
    static int numThreads;

    private static void executeCode(Runnable r, int numThreads) {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        tpe.prestartAllCoreThreads();
        IntStream.range(0, numThreads).forEach(( i ) -> tpe.execute(r));
        try {
            tpe.awaitTermination(1000, TimeUnit.MILLISECONDS);            
        } catch (Exception e) {}
        tpe.shutdown();
    }

    public static void main(String[] args) {

        // check and parse argument
        if (args.length == 0) {
            System.out.println("Usage " + Spmd.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        numThreads = Integer.parseInt(args[0]);
        if (numThreads < 1) {
            System.out.println("Usage " + Spmd.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        // launch the threads
        executeCode(() -> {
                threadName = Thread.currentThread().getName();
                Thread.yield();
                String message = "Hello from " +  threadName + " from a pool of " + numThreads;
                System.out.println(message);
           }, numThreads);
        System.out.println("Done.");
    }
}