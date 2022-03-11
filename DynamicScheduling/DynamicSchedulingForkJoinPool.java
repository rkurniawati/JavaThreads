import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class DynamicSchedulingForkJoinPool {
    static final int REPS = 16;

    static void sleepALittle(int numMillis) {
        try { 
            Thread.sleep(numMillis); 
        } catch(InterruptedException e) {
            // do nothing
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

        Random r = new Random();
        List<Callable<Void>> tasks = new ArrayList<>();
        for(int i = 0; i < numReps; i++) {
            // every third thread have longer sleep
            final int sleepTime = ((i % 3 == 0) ? 1 : 100);

            tasks.add(() -> {
                System.out.println("Thread " + Thread.currentThread().getName() + " about to sleep " + sleepTime + " ms");
                sleepALittle(sleepTime);
                System.out.println("Thread " + Thread.currentThread().getName() + " finished sleeping " + sleepTime + " ms");
                return null;
            });
        }

        fjp.invokeAll(tasks);

        long steals = ForkJoinPool.commonPool().getStealCount();
        System.out.println("Steal count " + steals);
        
    }
}
