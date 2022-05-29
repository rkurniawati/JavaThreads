import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class ReductionRace {
    private static final long SIZE = 1_000_000;
    private static final int MAX = 1_000;

    // the total in sequential and parallel
    private static int seqTotal, parTotal, parCustomPooltotal;

    static void sequentialSum(List<Integer> randomInts) {
        randomInts.forEach(
            i -> seqTotal += i
        );
    }

    // parallel sum using a common pool
    static void parallelSum1(List<Integer> randomInts) {
        randomInts.parallelStream().forEach(
            i -> parTotal += i
        );
    }

    // parallel sum using a custom pool
    static void parallelSumCustomPool(List<Integer> randomInts, ForkJoinPool customThreadPool) {
        try {
            customThreadPool.submit(() -> {
                randomInts.parallelStream().forEach(
                    i -> parCustomPooltotal += i
                );
                return null;
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // generate a stream of random integer in [0..MAX)
        List<Integer> randomInts = (new Random()).ints(0, MAX).limit(SIZE)
                .boxed().collect(Collectors.toList());

        // sequential 
        long startTime = System.currentTimeMillis();
        sequentialSum(randomInts);
        long seqTime = System.currentTimeMillis() - startTime;
        System.out.println("Seq total " + seqTotal + ", calculated in " + seqTime + " ms.");

        // using common pool
        startTime = System.currentTimeMillis();
        parallelSum1(randomInts);
        long parTime1 = System.currentTimeMillis() - startTime;
        System.out.println("Parallel total " + parTotal + ", calculated in " + parTime1 + " ms.");

        // use custom pool
        ForkJoinPool customThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        startTime = System.currentTimeMillis();
        parallelSumCustomPool(randomInts, customThreadPool);
        long parTime2 = System.currentTimeMillis() - startTime;
        System.out.println("Parallel total " + parCustomPooltotal + ", calculated in " + parTime2 + " ms.");
    }
}