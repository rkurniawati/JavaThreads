import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

class Spmd {
    public static void main(String[] args) {

        // check and parse argument
        if (args.length == 0) {
            System.out.println("Usage " + Spmd.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        int numThreads = Integer.parseInt(args[0]);
        if (numThreads < 1) {
            System.out.println("Usage " + Spmd.class.getName() + " numThreads.");
            System.out.println("Number of threads should be >= 1");
            return;
        }

        // launch the threads

        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        IntStream.range(0, numThreads).forEach(( i ) -> tpe.execute(() -> {
            String id = Thread.currentThread().getName();
            System.out.println("Hello from " + id + " from a pool of " + numThreads);
        }));
        tpe.shutdown();
        System.out.println("Done.");
    }
}