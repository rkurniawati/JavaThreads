import java.util.stream.IntStream;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;

class IntegrationRace {
    static double integral; // variable to accumulate answer

    static double f(double x) {
        return Math.sin(x);
    }
    
    public static void main(String[] args) {
        //Variables

        final double a = 0.0, b = Math.PI;         //limits of integration
        int n = 1048576;                           //number of subdivisions = 2^20
        final double h = (b - a) / n;              //width of each subdivision

        //sum up all the trapezoids
        List<Integer> indexes = IntStream.range(0, n+1).mapToObj(i->i)
            .collect(Collectors.toList());
        indexes.parallelStream().forEach(i -> integral += f(a+i*h));;

        integral = integral * h;
        System.out.printf("With %d trapezoids, our estimate of the integral from \n", n);
        System.out.printf("%f to %f is %f\n", a,b,integral);
    } 
}