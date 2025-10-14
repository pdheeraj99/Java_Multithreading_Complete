import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 🚀 Atomic Operations - The Solution with `AtomicInteger`
 *
 * `RaceConditionOnIncrement.java` lo chusina problem ni, ippudu manam
 * `java.util.concurrent.atomic.AtomicInteger` class tho solve cheddam.
 *
 * The Solution:
 * - `int count` ki badulu, manam `AtomicInteger count` ni use chestam.
 * - `count++` ki badulu, manam `count.getAndIncrement()` ni call chestam.
 *
 * How it Works:
 * - `getAndIncrement()` anedi lopalikalla (internally) CPU yokka special
 *   Compare-And-Swap (CAS) instruction ni use chestundi.
 * - Ee operation antha lock lekunda, thread-safe ga jarugutundi.
 * - Ee CAS operation valla, ఏ increment kuda lost avvadu.
 *
 * The final result will now always be exactly 100,000.
 */
class SafeCounter {
    // Use the thread-safe AtomicInteger instead of a primitive int.
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        // getAndIncrement() is an atomic operation.
        // It's equivalent to the post-increment operator (i++).
        // There's also incrementAndGet() for pre-increment (++i).
        count.getAndIncrement();
    }

    public int getCount() {
        // .get() returns the current value.
        return count.get();
    }
}

public class AtomicIncrementSolution {
    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 10;
        int incrementsPerThread = 10000;
        int expectedCount = numberOfThreads * incrementsPerThread;

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        SafeCounter counter = new SafeCounter();

        System.out.println("Starting threads to increment the SAFE atomic counter...");

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

        if (counter.getCount() == expectedCount) {
            System.out.println("\n🟢 SUCCESS: No increments were lost! The atomic operation worked perfectly.");
        } else {
            System.out.println("\n🔴 FAILURE: This should not happen with AtomicInteger.");
        }
    }
}

/*
✅ Expected Output:

Starting threads to increment the SAFE atomic counter...

--- Results ---
Expected final count: 100000
Actual final count:   100000

🟢 SUCCESS: No increments were lost! The atomic operation worked perfectly.
*/