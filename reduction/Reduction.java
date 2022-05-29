import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Reduction {
    private static final long SIZE = 1_000_000;
    private static final int MAX = 1_000;

    static int sequentialSum(List<Integer> randomInts) {
        return randomInts.stream().reduce(0, Integer::sum);
    }

    // parallel sum using a common pool
    static int parallelSum1(List<Integer> randomInts) {
        return randomInts.parallelStream().reduce(0, Integer::sum);
    }

    // parallel sum using a custom pool
    static int parallelSum2(List<Integer> randomInts, ForkJoinPool customThreadPool) {
        try {
            return customThreadPool.submit(() ->
                randomInts.parallelStream().reduce(0, Integer::sum)
            ).get(); // make sure that the task completes before we continue
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static void main(String[] args) {
        // generate a stream of random integer in [0..MAX)
        List<Integer> randomInts = (new Random()).ints(0, MAX).limit(SIZE)
                .boxed().collect(Collectors.toList());

        // sequential 
        long startTime = System.currentTimeMillis();
        int seqTotal = sequentialSum(randomInts);
        long seqTime = System.currentTimeMillis() - startTime;
        System.out.println("Seq total " + seqTotal + ", calculated in " + seqTime + " ms.");

        // using common pool
        startTime = System.currentTimeMillis();
        int parTotal1 = parallelSum1(randomInts);
        long parTime1 = System.currentTimeMillis() - startTime;
        System.out.println("Parallel total " + parTotal1 + ", calculated in " + parTime1 + " ms.");

        // use custom pool
        ForkJoinPool customThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        startTime = System.currentTimeMillis();
        int parTotal2 = parallelSum2(randomInts, customThreadPool);
        long parTime2 = System.currentTimeMillis() - startTime;

        System.out.println("Parallel total " + parTotal2 + ", calculated in " + parTime2 + " ms.");
    }

}