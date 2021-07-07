import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class MasterWorker {
    public static void main(String[] args) {

        // check and parse argument
        if (args.length == 0) {
            System.out.println("Usage " + MasterWorker.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        int numThreads = Integer.parseInt(args[0]);
        if (numThreads < 1) {
            System.out.println("Usage " + MasterWorker.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        // launch the threads
        executeCode(() -> {
                String threadName = Thread.currentThread().getName();
                
                System.out.println("Greetings from a worker, "+ threadName +" of "+ numThreads+" threads");
           }, numThreads);
        System.out.println("Greetings from the main thread: "+ Thread.currentThread().getName());
        System.out.println("Done.");

        shutdown();
    }

    static ThreadPoolExecutor tpe;

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
