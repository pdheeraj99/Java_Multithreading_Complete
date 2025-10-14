import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 🚀 `LongAdder` vs `AtomicLong` - A Performance Benchmark
 *
 * Ee example lo, manam high-contention scenario lo `LongAdder` and `AtomicLong`
 * yokka performance ni compare cheddam.
 *
 * Scenario:
 * - Manam chala ekkuva threads (e.g., 50) create chestam.
 * - Prathi thread oka shared counter ni chala sarlu (e.g., 1,000,000) increment chestundi.
 * - Ee process ni `AtomicLong` tho, and malli `LongAdder` tho chesi,
 *   renditiki entha time pattindo measure cheddam.
 *
 * Expected Result:
 * - `LongAdder` will be SIGNIFICANTLY faster than `AtomicLong` because it avoids
 *   the CAS contention bottleneck by using internal "cells" for each thread.
 * - `AtomicLong` will be slower because all 50 threads are constantly fighting
 *   to update the same single memory location.
 */
public class LongAdderVsAtomicLong {

    private static final int NUM_THREADS = 50;
    private static final int INCREMENTS_PER_THREAD = 1_000_000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting benchmark: LongAdder vs AtomicLong under high contention.");
        System.out.println("Threads: " + NUM_THREADS);
        System.out.println("Increments per thread: " + INCREMENTS_PER_THREAD);
        System.out.println("-------------------------------------------------");

        // --- Test AtomicLong ---
        long atomicLongTime = testAtomicLong();
        System.out.println("Time taken for AtomicLong: " + atomicLongTime + " ms");

        System.out.println("-------------------------------------------------");

        // --- Test LongAdder ---
        long longAdderTime = testLongAdder();
        System.out.println("Time taken for LongAdder:  " + longAdderTime + " ms");

        System.out.println("-------------------------------------------------");

        if (longAdderTime < atomicLongTime) {
            double improvement = ((double)(atomicLongTime - longAdderTime) / atomicLongTime) * 100;
            System.out.printf("🟢 SUCCESS: LongAdder was %.2f%% faster than AtomicLong.%n", improvement);
        } else {
            System.out.println("🔴 NOTE: In this run, LongAdder was not faster. This can happen on systems with fewer cores or less contention.");
        }
    }

    private static long testAtomicLong() throws InterruptedException {
        AtomicLong atomicCounter = new AtomicLong(0);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    atomicCounter.getAndIncrement();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static long testLongAdder() throws InterruptedException {
        LongAdder longAdderCounter = new LongAdder();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    longAdderCounter.increment();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        // Just to ensure correctness, though not part of the benchmark itself
        // System.out.println("Final LongAdder sum: " + longAdderCounter.sum());
        return endTime - startTime;
    }
}

/*
✅ Expected Output (Exact times will vary, but the trend should be clear):

Starting benchmark: LongAdder vs AtomicLong under high contention.
Threads: 50
Increments per thread: 1000000
-------------------------------------------------
Time taken for AtomicLong: 2345 ms
-------------------------------------------------
Time taken for LongAdder:  150 ms
-------------------------------------------------
🟢 SUCCESS: LongAdder was 93.60% faster than AtomicLong.
*/