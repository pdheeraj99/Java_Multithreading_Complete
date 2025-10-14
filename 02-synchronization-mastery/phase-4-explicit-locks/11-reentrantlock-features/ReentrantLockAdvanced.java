import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 🚀 `ReentrantLock` - Advanced Features Demonstration
 *
 * Ee example lo, manam `ReentrantLock` yokka advanced features ni chuddam:
 * 1. `tryLock()` - The non-blocking, timed lock attempt.
 * 2. `lockInterruptibly()` - The lock attempt that can be cancelled.
 */
public class ReentrantLockAdvanced {

    public static void main(String[] args) {
        System.out.println("--- 1. Demonstrating tryLock() ---");
        demonstrateTryLock();

        System.out.println("\n\n--- 2. Demonstrating lockInterruptibly() ---");
        demonstrateLockInterruptibly();
    }

    /**
     * Demonstrates `tryLock()`. A thread will hold a lock, and another thread
     * will try to acquire it but will give up after a timeout.
     */
    public static void demonstrateTryLock() {
        final ReentrantLock lock = new ReentrantLock();

        // Thread-1 acquires the lock and holds it for a long time.
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread-1: Attempting to acquire the lock...");
            lock.lock();
            System.out.println("Thread-1: Acquired the lock. Going to sleep for 5 seconds...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println("Thread-1: Released the lock.");
            }
        }, "Thread-1");

        // Thread-2 tries to acquire the lock, but with a timeout.
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread-2: Attempting to acquire the lock with a 2-second timeout...");
            boolean lockAcquired = false;
            try {
                // Try to get the lock, but only wait for 2 seconds.
                lockAcquired = lock.tryLock(2, TimeUnit.SECONDS);
                if (lockAcquired) {
                    System.out.println("Thread-2: Acquired the lock! (This shouldn't happen in this scenario)");
                    // ... do work ...
                } else {
                    System.out.println("Thread-2: FAILED to acquire the lock within 2 seconds. Doing alternative work.");
                }
            } catch (InterruptedException e) {
                System.out.println("Thread-2: Was interrupted while waiting for the lock.");
                Thread.currentThread().interrupt();
            } finally {
                if (lockAcquired) {
                    lock.unlock();
                    System.out.println("Thread-2: Released the lock.");
                }
            }
        }, "Thread-2");

        thread1.start();
        // Give Thread-1 a moment to acquire the lock
        try { Thread.sleep(100); } catch (Exception e) {}
        thread2.start();
    }

    /**
     * Demonstrates `lockInterruptibly()`. A thread will be waiting for a lock,
     * and we will interrupt it from the main thread.
     */
    public static void demonstrateLockInterruptibly() {
        final ReentrantLock lock = new ReentrantLock();

        // Thread-A acquires the lock and holds it.
        Thread threadA = new Thread(() -> {
            lock.lock();
            System.out.println("Thread-A: Acquired and holding the lock...");
            // Keep the lock until the program ends
            try { Thread.sleep(10000); } catch (InterruptedException e) {}
            lock.unlock();
        }, "Thread-A");

        // Thread-B tries to acquire the lock using lockInterruptibly(), so it can be cancelled.
        Thread threadB = new Thread(() -> {
            System.out.println("Thread-B: Attempting to acquire the lock (interruptibly)...");
            try {
                lock.lockInterruptibly();
                System.out.println("Thread-B: Acquired the lock. (This shouldn't happen in this scenario)");
                lock.unlock();
            } catch (InterruptedException e) {
                System.out.println("Thread-B: SUCCESS! My wait for the lock was interrupted from outside!");
                System.out.println("Thread-B: I can now stop waiting and do something else useful.");
                Thread.currentThread().interrupt();
            }
        }, "Thread-B");

        threadA.start();
        // Give Thread-A a moment to acquire the lock
        try { Thread.sleep(100); } catch (Exception e) {}
        threadB.start();

        // Let Thread-B wait for a couple of seconds
        try { Thread.sleep(2000); } catch (Exception e) {}

        // Now, let's cancel Thread-B's wait.
        System.out.println("Main: Interrupting Thread-B, which is waiting for the lock.");
        threadB.interrupt();
    }
}

/*
✅ Expected Output:

--- 1. Demonstrating tryLock() ---
Thread-1: Attempting to acquire the lock...
Thread-1: Acquired the lock. Going to sleep for 5 seconds...
Thread-2: Attempting to acquire the lock with a 2-second timeout...
Thread-2: FAILED to acquire the lock within 2 seconds. Doing alternative work.
Thread-1: Released the lock.


--- 2. Demonstrating lockInterruptibly() ---
Thread-A: Acquired and holding the lock...
Thread-B: Attempting to acquire the lock (interruptibly)...
Main: Interrupting Thread-B, which is waiting for the lock.
Thread-B: SUCCESS! My wait for the lock was interrupted from outside!
Thread-B: I can now stop waiting and do something else useful.
*/