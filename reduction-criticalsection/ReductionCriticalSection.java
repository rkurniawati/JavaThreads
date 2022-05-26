import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class ReductionCriticalSection {
    private static final long SIZE = 1_000_000;
    private static final int MAX = 1_000;

    static class Total {
        int total;

        // critical section
        synchronized void add(int i) {
            total += i;
        }
        public String toString() {
            return Integer.toString(total);
        }
    }

    // the total in sequential and parallel
    private static Total seqTotal = new Total(), parTotal1 = new Total(), parTotal2 = new Total();

    static void sequentialSum(List<Integer> randomInts) {
        randomInts.stream().forEach(i->seqTotal.add(i));
    }

    // parallel sum using a common pool
    static void parallelSum1(List<Integer> randomInts) {
        randomInts.parallelStream().forEach(
                i-> parTotal1.add(i)
        );
    }

    // parallel sum using a custom pool
    static void parallelSum2(List<Integer> randomInts, ForkJoinPool customThreadPool) {
        try {
            customThreadPool.submit(
                    () -> randomInts.parallelStream().forEach(i->parTotal2.add(i))
            ).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // generate a stream of random integer in [0..MAX)
        List<Integer> randomInts = (new Random(123)).ints(0, MAX).limit(SIZE)
                .mapToObj(x -> x).collect(Collectors.toList());

        // sequential 
        long startTime = System.currentTimeMillis();
        sequentialSum(randomInts);
        long seqTime = System.currentTimeMillis() - startTime;
        System.out.println("Seq total " + seqTotal + ", calculated in " + seqTime + " ms.");

        // using common pool
        startTime = System.currentTimeMillis();
        parallelSum1(randomInts);
        long parTime1 = System.currentTimeMillis() - startTime;
        System.out.println("Parallel total " + parTotal1 + ", calculated in " + parTime1 + " ms.");

        // use custom pool
        ForkJoinPool customThreadPool = new ForkJoinPool(16);
        startTime = System.currentTimeMillis();
        parallelSum2(randomInts, customThreadPool);
        long parTime2 = System.currentTimeMillis() - startTime;
        System.out.println("Parallel total " + parTotal2 + ", calculated in " + parTime2 + " ms.");
    }
}