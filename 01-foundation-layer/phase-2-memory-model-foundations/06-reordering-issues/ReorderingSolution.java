/**
 * 🚀 Reordering Issues - The Solution using 'volatile'
 *
 * Manam `ReorderingProblem.java` lo chusina "impossible" state (`a=0, b=0`) ni
 * `volatile` keyword use chesi ela prevent cheyalo ee example lo chuddam.
 *
 * The Fix:
 * - `x` and `y` variables ni `volatile` ga declare cheddam.
 * - JMM (Java Memory Model) ప్రకారం, `volatile` write ki, `volatile` read ki madhya
 *   oka happens-before relationship untundi.
 * - Crucially, `volatile` prevents the reordering of writes with other memory operations
 *   in many contexts, including this one.
 * - When `x` is volatile, `x=1` cannot be reordered with `a=y`.
 * - When `y` is volatile, `y=1` cannot be reordered with `b=x`.
 *
 * Ee change tho, `a=0` and `b=0` ane state inka impossible avuthundi.
 * The cycle we discussed (`a=y -> y=1 -> b=x -> x=1 -> a=y`) is now prevented by
 * the memory barriers that `volatile` introduces.
 *
 * The program will now run forever without ever finding the "impossible" state,
 * because with `volatile`, it truly is impossible.
 */
public class ReorderingSolution {

    // The FIX: Declare the shared variables as volatile.
    private volatile int x = 0;
    private volatile int y = 0;

    // These variables are thread-local, so they don't need to be volatile.
    private int a = 0;
    private int b = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting reordering demonstration with the 'volatile' fix.");
        System.out.println("With this fix, the state a=0, b=0 is truly impossible.");
        System.out.println("The program will loop infinitely without finding the error.");
        System.out.println("Press Ctrl+C to stop the test.");

        final ReorderingSolution test = new ReorderingSolution();
        long iteration = 0;

        while (true) {
            iteration++;
            test.x = 0; test.y = 0;
            test.a = 0; test.b = 0;

            Thread thread1 = new Thread(() -> {
                // With volatile, the following operations have stricter ordering guarantees.
                test.x = 1;
                test.a = test.y;
            });

            Thread thread2 = new Thread(() -> {
                test.y = 1;
                test.b = test.x;
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            // This condition will now never be true.
            if (test.a == 0 && test.b == 0) {
                System.out.println("-------------------------------------------");
                System.out.println("🔴 This line should be unreachable!");
                System.out.println("If you see this, something is deeply wrong with the JVM/hardware.");
                System.out.println("-------------------------------------------");
                break;
            }

            if (iteration % 500000 == 0) {
                System.out.println("Completed " + iteration + " iterations. As expected, no reordering detected.");
            }
        }
    }
}