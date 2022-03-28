import java.util.stream.IntStream;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;
import java.util.concurrent.ForkJoinPool;

class IntegrationRaceCustomPool {
    static double integral; // variable to accumulate answer

    static double f(double x) {
        return Math.sin(x);
    }
    
    public static void main(String[] args) throws Exception {
        int numThreads = Runtime.getRuntime().availableProcessors();
        if (args.length >= 1) {
            numThreads = Integer.parseInt(args[0]);
            if (numThreads < 1) {
                System.out.println("Usage " + IntegrationRaceCustomPool.class.getName() + " numThreads.");
                System.out.println("Number of threads should be >= 1");
                return;
            }
        }
        System.out.println("Number of threads " + numThreads);


        //Variables

        final double a = 0.0, b = Math.PI;         //limits of integration
        int n = 1048576;                           //number of subdivisions = 2^20
        final double h = (b - a) / n;              //width of each subdivision

        //sum up all the trapezoids
        ForkJoinPool customThreadPool = new ForkJoinPool(numThreads);

        List<Integer> indexes = IntStream.range(0, n+1).mapToObj(i->i)
            .collect(Collectors.toList());
        customThreadPool.submit(
            () -> indexes.parallelStream().forEach(i -> integral += f(a+i*h))
        ).get();

        integral = integral * h;
        System.out.printf("With %d trapezoids, our estimate of the integral from \n", n);
        System.out.printf("%f to %f is %f\n", a,b,integral);
    } 
}