import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ForkJoin {

    public static void main(String[] args) {
        System.out.println("\nBeginning\n");
        executeCode(() -> System.out.println("Part I"), 2);
        shutdown();
    
        System.out.println("\n\nBetween I and II...\n");
        executeCode(() -> System.out.println("Part II"), 3);
        shutdown();
        
        System.out.println("\n\nBetween II and III...\n");
        executeCode(() -> System.out.println("Part III"), 5);
        shutdown();
        
        System.out.println("\n\nEnd\n\n");
    }

    private static ThreadPoolExecutor tpe;

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
