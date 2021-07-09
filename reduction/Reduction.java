import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Reduction {
    private static final long SIZE = 1_000_000;
    private static final int MAX = 1_000;

    public static void main(String[] args) {
        // check and parse argument
        int numThreads = Runtime.getRuntime().availableProcessors();
        if (args.length < 1) {
            System.out.println("Usage " + Reduction.class.getName() + " numThreads.");
            System.out.println("Using default number of Threads " + numThreads);
        } else {
            numThreads = Integer.parseInt(args[0]);
        }

        // generate a stream of random integer in [0..MAX)
        List<Integer> randomInts = (new Random()).ints(0, MAX).limit(SIZE)
                .mapToObj(x -> x).collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        int seqTotal = randomInts.stream().reduce(0, Integer::sum);
        long seqTime = System.currentTimeMillis() - startTime;
        System.out.println("Seq total " + seqTotal + ", calculated in " + seqTime + " ms.");

        startTime = System.currentTimeMillis();
        ForkJoinPool customThreadPool = new ForkJoinPool(numThreads);
        int parTotal = 0;
        try {
            parTotal = customThreadPool.submit(
                    () -> randomInts.parallelStream().reduce(0, Integer::sum)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        long parTime = System.currentTimeMillis() - startTime;

        System.out.println("Parallel total " + parTotal + ", calculated in " + parTime + " ms.");
    }
}