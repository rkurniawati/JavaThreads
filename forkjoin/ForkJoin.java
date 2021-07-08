import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ForkJoin {

    static class Part1 implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            System.out.println(Thread.currentThread().getName()  + ": Part I");
            return null;
        }
    }

    static class Part2 implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            System.out.println(Thread.currentThread().getName()  + ": Part II");
            return null;
        }
    }

    static class Part3 implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            System.out.println(Thread.currentThread().getName()  + ": Part III");
            return null;
        }
    }

    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool(5);
        System.out.println("\nBeginning\n");
        fjp.invokeAll(Arrays.asList(new Part1(), new Part1()));

        System.out.println("\n\nBetween I and II...\n");
        fjp.invokeAll(Arrays.asList(new Part2(), new Part2(), new Part2()));

        System.out.println("\n\nBetween II and III...\n");
        fjp.invokeAll(Arrays.asList(new Part3(), new Part3(), new Part3(), new Part3(), new Part3()));

        System.out.println("\n\nEnd\n\n");
    }
}
