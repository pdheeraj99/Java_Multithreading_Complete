import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 🚀 `ReadWriteLock` - Demonstration
 *
 * Ee example lo, manam `ReadWriteLock` ni use chesi, multiple readers concurrently
 * and a single writer exclusively access cheyadam chuddam.
 *
 * Scenario:
 * - Oka `SharedResource` undi, daaniki oka `message` undi.
 * - Multiple "Reader" threads will try to read this message simultaneously.
 * - One "Writer" thread will occasionally update this message.
 *
 * Observation:
 * - Reader threads will be able to acquire the read lock and execute concurrently.
 *   You will see multiple "reading..." messages at the same time.
 * - When the Writer thread acquires the write lock, all other threads (readers and writers)
 *   will be blocked until the writer releases the lock.
 */
class SharedResource {
    private String message = "Hello, this is the initial message.";

    // Step 1: Create a ReentrantReadWriteLock instance.
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    // Step 2: Extract the read lock and write lock from it.
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    // --- Reader Method ---
    public void readMessage() {
        System.out.println(Thread.currentThread().getName() + " is trying to acquire the read lock.");
        readLock.lock();
        try {
            System.out.println("  " + Thread.currentThread().getName() + " acquired the read lock and is reading...");
            System.out.println("  Current Message: " + this.message);
            // Simulate reading time
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("  " + Thread.currentThread().getName() + " is releasing the read lock.");
            readLock.unlock();
        }
    }

    // --- Writer Method ---
    public void writeMessage() {
        System.out.println(Thread.currentThread().getName() + " is trying to acquire the WRITE lock.");
        writeLock.lock();
        try {
            System.out.println("    " + Thread.currentThread().getName() + " acquired the WRITE lock. All readers are blocked now.");
            this.message = "The message was updated at " + new Date();
            System.out.println("    " + Thread.currentThread().getName() + " has updated the message.");
            // Simulate writing time
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("    " + Thread.currentThread().getName() + " is releasing the WRITE lock.");
            writeLock.unlock();
        }
    }
}

public class ReadWriteLockExample {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();
        // Create a thread pool with enough threads to see concurrency
        ExecutorService executor = Executors.newFixedThreadPool(6);

        // Submit 5 reader tasks
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> resource.readMessage());
        }

        // Submit 1 writer task
        executor.submit(() -> resource.writeMessage());

        executor.shutdown();
    }
}

/*
✅ Expected Output (order might vary slightly, but the core pattern will be the same):

// Notice multiple readers can acquire the lock at the same time
Reader-1 is trying to acquire the read lock.
  Reader-1 acquired the read lock and is reading...
  Current Message: Hello, this is the initial message.
Reader-2 is trying to acquire the read lock.
  Reader-2 acquired the read lock and is reading...
  Current Message: Hello, this is the initial message.
... (more readers)

Writer-1 is trying to acquire the WRITE lock.
// Readers will release their locks
  Reader-1 is releasing the read lock.
  Reader-2 is releasing the read lock.
...
// NOW the writer can get the lock
    Writer-1 acquired the WRITE lock. All readers are blocked now.
    Writer-1 has updated the message.
    Writer-1 is releasing the WRITE lock.

// Now any remaining readers can proceed
Reader-5 is trying to acquire the read lock.
  Reader-5 acquired the read lock and is reading...
  Current Message: The message was updated at ...
  Reader-5 is releasing the read lock.
*/