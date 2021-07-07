import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Barrier {

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
            System.out.println("Usage " + Barrier.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        int numThreads = Integer.parseInt(args[0]);
        if (numThreads < 1) {
            System.out.println("Usage " + Barrier.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        final CyclicBarrier cb = new CyclicBarrier(numThreads);
        // launch the threads
        executeCode(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Thread " +  threadName + " from a pool of " + numThreads + " BEFORE the barrier");
                try {
                    cb.await();                    
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.err.println(e.toString());
                }
                System.out.println("Thread " +  threadName + " from a pool of " + numThreads + " AFTER the barrier");
           }, numThreads);
        System.out.println("Done.");
    }
    
}
