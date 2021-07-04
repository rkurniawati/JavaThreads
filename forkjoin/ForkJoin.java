import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ForkJoin {
    private static void executeCode(Runnable r, int numThreads) {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);

        IntStream.range(0, numThreads).forEach(( i ) -> tpe.execute(r));
        try {
            tpe.awaitTermination(1000, TimeUnit.MILLISECONDS);            
        } catch (Exception e) {}
        //tpe.shutdown();
    }

    public static void main(String[] args) {
        System.out.println("\nBeginning\n");
        executeCode(() -> System.out.println("Part I"), 2);
    
        System.out.println("\n\nBetween I and II...\n");
        executeCode(() -> System.out.println("Part II"), 3);
        
        System.out.println("\n\nBetween II and III...\n");
        executeCode(() -> System.out.println("Part III"), 5);

        System.out.println("\n\nEnd\n\n");
    }
}
