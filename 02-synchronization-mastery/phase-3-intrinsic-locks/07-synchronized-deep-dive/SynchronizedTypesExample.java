import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 🚀 `synchronized` Keyword - Live Demonstration
 *
 * Ee example lo, manam three types of synchronization ni live ga chuddam.
 * 1. Synchronized Instance Method
 * 2. Synchronized Block
 * 3. Synchronized Static Method
 */

// --- 1. Synchronized Instance Method ---
class InstanceMethodSynchronizer {
    private int count = 0;

    // Ee method `this` object meeda synchronize cheyyabadindi.
    // Oka thread ee method lo unnappudu, vere thread ade object meeda ee method ni call cheyaledu.
    public synchronized void increment() {
        count++;
        System.out.println("Instance Method: Thread " + Thread.currentThread().getName() + " incremented count to " + count);
    }

    public int getCount() {
        return count;
    }
}

// --- 2. Synchronized Block ---
class BlockSynchronizer {
    private int count = 0;
    // Best practice: Use a private, final object for locking.
    private final Object lock = new Object();

    public void doWork() {
        System.out.println("Block Sync: Thread " + Thread.currentThread().getName() + " is doing non-critical work.");
        // Non-critical work ikkada cheyochu, lock lekunda.

        // Only the critical section is synchronized.
        synchronized (lock) {
            count++;
            System.out.println("Block Sync: Thread " + Thread.currentThread().getName() + " holds the lock and incremented count to " + count);
        }
    }

    public int getCount() {
        return count;
    }
}

// --- 3. Synchronized Static Method ---
class StaticMethodSynchronizer {
    // A static variable shared by all instances of the class.
    private static int staticCount = 0;

    // Ee method `StaticMethodSynchronizer.class` object meeda synchronize cheyyabadindi.
    // Ee lock application anthatiki okate untundi.
    public static synchronized void increment() {
        staticCount++;
        System.out.println("Static Method: Thread " + Thread.currentThread().getName() + " incremented staticCount to " + staticCount);
    }

    public static int getStaticCount() {
        return staticCount;
    }
}


public class SynchronizedTypesExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(6);

        System.out.println("--- Testing Synchronized Instance Method ---");
        InstanceMethodSynchronizer instanceSync = new InstanceMethodSynchronizer();
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> instanceSync.increment());
        }

        // Wait for a moment before starting the next test
        Thread.sleep(1000);

        System.out.println("\n--- Testing Synchronized Block ---");
        BlockSynchronizer blockSync = new BlockSynchronizer();
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> blockSync.doWork());
        }

        // Wait for a moment before starting the next test
        Thread.sleep(2000);

        System.out.println("\n--- Testing Synchronized Static Method ---");
        for (int i = 0; i < 5; i++) {
            // All threads will call the same static method, competing for the same class-level lock.
            executor.submit(() -> StaticMethodSynchronizer.increment());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n--- Final Counts ---");
        System.out.println("Instance Method Final Count: " + instanceSync.getCount());
        System.out.println("Block Sync Final Count: " + blockSync.getCount());
        System.out.println("Static Method Final Count: " + StaticMethodSynchronizer.getStaticCount());
    }
}

/*
✅ Expected Output (The order of lines within each block will vary, but the counts will be correct):

--- Testing Synchronized Instance Method ---
Instance Method: Thread pool-1-thread-1 incremented count to 1
Instance Method: Thread pool-1-thread-2 incremented count to 2
Instance Method: Thread pool-1-thread-3 incremented count to 3
Instance Method: Thread pool-1-thread-4 incremented count to 4
Instance Method: Thread pool-1-thread-5 incremented count to 5

--- Testing Synchronized Block ---
Block Sync: Thread pool-1-thread-6 is doing non-critical work.
Block Sync: Thread pool-1-thread-6 holds the lock and incremented count to 1
Block Sync: Thread pool-1-thread-1 is doing non-critical work.
Block Sync: Thread pool-1-thread-1 holds the lock and incremented count to 2
... (and so on for 5 threads)

--- Testing Synchronized Static Method ---
Static Method: Thread pool-1-thread-3 incremented staticCount to 1
Static Method: Thread pool-1-thread-2 incremented staticCount to 2
... (and so on for 5 threads)

--- Final Counts ---
Instance Method Final Count: 5
Block Sync Final Count: 5
Static Method Final Count: 5
*/