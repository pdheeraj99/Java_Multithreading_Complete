import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 🚀 Race Condition on `i++` - Live Demonstration
 *
 * Ee example lo, manam `i++` anedi atomic kaadu ani live ga chuddam.
 * Multiple threads okate non-thread-safe counter ni increment cheste,
 * race conditions valla konni increments "lost" avuthayi.
 *
 * Scenario:
 * - Manam 10 threads create chestam.
 * - Prathi thread oka shared counter ni 10,000 sarlu increment chestundi.
 * - Total ga, counter 10 * 10,000 = 100,000 sarlu increment avvali.
 *
 * The Problem:
 * - Counter anedi oka simple `int`. `count++` anedi atomic operation kaadu.
 * - As explained in the markdown, this is a three-step "read-modify-write" sequence.
 * - Multiple threads ee sequence ni at the same time execute cheste, increments lost avuthayi.
 *
 * The final result will almost certainly be less than 100,000.
 */
class UnsafeCounter {
    private int count = 0;

    public void increment() {
        // This is the non-atomic operation
        count++;
    }

    public int getCount() {
        return count;
    }
}

public class RaceConditionOnIncrement {
    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 10;
        int incrementsPerThread = 10000;
        int expectedCount = numberOfThreads * incrementsPerThread;

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        UnsafeCounter counter = new UnsafeCounter();

        System.out.println("Starting threads to increment the unsafe counter...");

        // Submit tasks to all threads
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n--- Results ---");
        System.out.println("Expected final count: " + expectedCount);
        System.out.println("Actual final count:   " + counter.getCount());

        if (counter.getCount() < expectedCount) {
            System.out.println("\n🔴 FAILURE: Race condition detected! " + (expectedCount - counter.getCount()) + " increments were lost.");
        } else {
            System.out.println("\n🟢 SUCCESS: No race condition detected. (This is highly unlikely and you just got lucky!)");
        }
    }
}

/*
✅ Expected Output (Actual count will vary slightly on each run):

Starting threads to increment the unsafe counter...

--- Results ---
Expected final count: 100000
Actual final count:   94573

🔴 FAILURE: Race condition detected! 5427 increments were lost.
*/