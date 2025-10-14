import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 🚀 `ReentrantLock` - Basic Usage Demonstration
 *
 * Ee example `ReentrantLock` yokka basic `lock()` and `unlock()` functionality ni చూపిస్తుంది.
 * The most important part to remember is the `try-finally` block to guarantee the lock is released.
 */
class Counter {
    private int count = 0;

    // Step 1: Create an instance of ReentrantLock.
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        // Step 2: Acquire the lock.
        // If the lock is held by another thread, this thread will block until it's available.
        lock.lock();

        // Step 3: Use a try-finally block to ensure the lock is always released.
        try {
            // This is the critical section.
            count++;
            System.out.println("Thread " + Thread.currentThread().getName() + " incremented count to: " + count);
        } finally {
            // Step 4: Release the lock in the finally block.
            // This is CRITICAL! It ensures that even if an exception occurs in the try block,
            // the lock is released, preventing other threads from deadlocking.
            lock.unlock();
            System.out.println("Thread " + Thread.currentThread().getName() + " released the lock.");
        }
    }

    public int getCount() {
        return count;
    }
}

public class ReentrantLockBasics {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        ExecutorService executor = Executors.newFixedThreadPool(5);

        System.out.println("Submitting 5 increment tasks to the executor.");

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> counter.increment());
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);

        if (finished) {
            System.out.println("\nAll tasks finished.");
            System.out.println("Final count should be 5. Final count is: " + counter.getCount());
        } else {
            System.out.println("Tasks did not finish in time.");
        }
    }
}

/*
✅ Expected Output (Order of lines will vary, but the final count will be correct):

Submitting 5 increment tasks to the executor.
Thread pool-1-thread-1 incremented count to: 1
Thread pool-1-thread-1 released the lock.
Thread pool-1-thread-2 incremented count to: 2
Thread pool-1-thread-2 released the lock.
Thread pool-1-thread-3 incremented count to: 3
Thread pool-1-thread-3 released the lock.
Thread pool-1-thread-4 incremented count to: 4
Thread pool-1-thread-4 released the lock.
Thread pool-1-thread-5 incremented count to: 5
Thread pool-1-thread-5 released the lock.

All tasks finished.
Final count should be 5. Final count is: 5
*/