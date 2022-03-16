import java.util.stream.IntStream;

public class DynamicSchedulingStream {
    static final int REPS = 16;

    static void sleepALittle(int numMillis) {
        try { 
            Thread.sleep(numMillis); 
        } catch(InterruptedException e) {
            // do nothing
        }
    }

    public static void main(String[] args) throws Exception {
        int numReps = 16;
        // check and parse argument
        if (args.length >= 1) {
            numReps = Integer.parseInt(args[0]);
        }

        IntStream.range(0, numReps)
                .parallel()
                .forEach(i -> {
                    final int sleepTime = ((i % 6 == 0) ? 1000 : 1);

                    System.out.println("Thread " + Thread.currentThread().getName() + " starting iteration " + i);
                    sleepALittle(sleepTime);
                    System.out.println("Thread " + Thread.currentThread().getName() + " completed iteration " + i);

                });
        System.out.println("Done.");

    }
}
