/**
 * 🚀 Reordering Issues - Live Demonstration
 *
 * Ee example reordering valla vachhe oka classic problem ni demonstrate chestundi.
 *
 * Scenario:
 * - Manaki `x`, `y`, `a`, and `b` ane four variables unnayi. Initial ga anni zero.
 * - Thread-1: `x` ni `1` ga set chesi, `a` lo `y` value ni read chestundi.
 * - Thread-2: `y` ni `1` ga set chesi, `b` lo `x` value ni read chestundi.
 *
 * Logic:
 * - Ee operations anni sequential ga jarigithe, `a=0, b=0` ane result eppudu raakudadu.
 * - Why? `a=0` avvalante, `a=y` anedi `y=1` kanna mundu jaragali.
 * - `b=0` avvalante, `b=x` anedi `x=1` kanna mundu jaragali.
 * - So, `a=y` (T1) -> `y=1` (T2) and `b=x` (T2) -> `x=1` (T1).
 * - Ee dependency chusthe, `a=y` -> `y=1` -> `b=x` -> `x=1` -> `a=y`... idi oka cycle. It's impossible.
 *
 * But what if reordering happens?
 * - Thread-1 lo, compiler `a=y` ni `x=1` kanna mundu execute cheyochu.
 * - Thread-2 lo, compiler `b=x` ni `y=1` kanna mundu execute cheyochu.
 *
 * Reordered Execution:
 * 1. Thread-1: `a = y;` (y is 0, so a becomes 0)
 * 2. Thread-2: `b = x;` (x is 0, so b becomes 0)
 * 3. Thread-1: `x = 1;`
 * 4. Thread-2: `y = 1;`
 *
 * Result: `a=0, b=0`. This is a state that should be impossible, but reordering makes it possible!
 *
 * NOTE: Ee bug ni trigger cheyadam chala chala kashtam. It requires thousands or millions of
 * iterations to see it happen even once. This code will run for a while and report if it
 * ever finds this "impossible" state.
 */
public class ReorderingExample {

    // No volatile, no synchronized. Plain variables.
    private int x = 0;
    private int y = 0;
    private int a = 0;
    private int b = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting reordering demonstration. This might take a while...");
        System.out.println("We are looking for the 'impossible' result: a=0, b=0.");
        System.out.println("Press Ctrl+C to stop the test.");

        final ReorderingExample test = new ReorderingExample();
        long iteration = 0;

        while (true) {
            iteration++;
            // Reset variables for each run
            test.x = 0; test.y = 0;
            test.a = 0; test.b = 0;

            Thread thread1 = new Thread(() -> {
                // These two lines can be reordered by the JVM/CPU
                test.x = 1;
                test.a = test.y;
            });

            Thread thread2 = new Thread(() -> {
                // These two lines can also be reordered
                test.y = 1;
                test.b = test.x;
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            // Check for the impossible result
            if (test.a == 0 && test.b == 0) {
                System.out.println("-------------------------------------------");
                System.out.println("🔴 IMPOSSIBLE STATE DETECTED on iteration " + iteration);
                System.out.println("Result: a = 0, b = 0");
                System.out.println("This is a proof of instruction reordering!");
                System.out.println("-------------------------------------------");
                break; // Stop after finding the first occurrence
            }

            // Print progress every 100,000 iterations
            if (iteration % 100000 == 0) {
                System.out.println("Completed " + iteration + " iterations, no reordering detected yet...");
            }
        }
    }
}

/*
✅ Expected Output (after a potentially long time):

Starting reordering demonstration. This might take a while...
We are looking for the 'impossible' result: a=0, b=0.
Press Ctrl+C to stop the test.
Completed 100000 iterations, no reordering detected yet...
Completed 200000 iterations, no reordering detected yet...
...
...
-------------------------------------------
🔴 IMPOSSIBLE STATE DETECTED on iteration 1234567
Result: a = 0, b = 0
This is a proof of instruction reordering!
-------------------------------------------
*/