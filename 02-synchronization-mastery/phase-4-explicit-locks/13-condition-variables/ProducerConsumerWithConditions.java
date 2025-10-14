import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 🚀 Producer-Consumer with `ReentrantLock` and `Condition` Variables
 *
 * Ee example lo, manam Producer-Consumer problem ni `ReentrantLock` and `Condition`
 * variables tho implement cheddam. Idi `wait/notify` version kanna more efficient
 * and scalable.
 *
 * How it Works:
 * - Manam oka `ReentrantLock` create chestam.
 * - Daani nunchi rendu separate `Condition` objects create chestam:
 *   1. `notFull`: Producers wait cheyadaniki (when the buffer is full).
 *   2. `notEmpty`: Consumers wait cheyadaniki (when the buffer is empty).
 *
 * The Magic:
 * - When a producer adds an item, it calls `notEmpty.signalAll()`. Idi *only* waiting
 *   consumers ni leputundi. Producers ni disturb cheyadu.
 * - When a consumer removes an item, it calls `notFull.signalAll()`. Idi *only* waiting
 *   producers ni leputundi. Consumers ni disturb cheyadu.
 *
 * Ee precision valla, gereksiz (unnecessary) wakeups undavu, making the application
 * perform better under high contention.
 */
class BoundedBuffer {
    private final Queue<Integer> buffer = new LinkedList<>();
    private final int capacity = 5;
    private int value = 0;

    // --- The Lock and Conditions ---
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public void produce() throws InterruptedException {
        while(true) {
            lock.lock();
            try {
                // Wait while the buffer is full.
                while (buffer.size() == capacity) {
                    System.out.println("Buffer is full. Producer " + Thread.currentThread().getName() + " is waiting on 'notFull' condition... 😴");
                    notFull.await(); // Releases the lock and waits on this specific condition.
                }

                // Add an item to the buffer.
                System.out.println("Producer " + Thread.currentThread().getName() + " produced: " + value);
                buffer.add(value++);

                // Signal to all waiting consumers that the buffer is no longer empty.
                System.out.println("Producer " + Thread.currentThread().getName() + " is signaling the 'notEmpty' condition. 🔔");
                notEmpty.signalAll();

            } finally {
                lock.unlock();
            }
            Thread.sleep(50); // Simulate work
        }
    }

    public void consume() throws InterruptedException {
        while(true) {
            lock.lock();
            try {
                // Wait while the buffer is empty.
                while (buffer.isEmpty()) {
                    System.out.println("Buffer is empty. Consumer " + Thread.currentThread().getName() + " is waiting on 'notEmpty' condition... 😴");
                    notEmpty.await(); // Releases the lock and waits on this specific condition.
                }

                // Remove an item from the buffer.
                int consumedValue = buffer.poll();
                System.out.println("Consumer " + Thread.currentThread().getName() + " consumed: " + consumedValue);

                // Signal to all waiting producers that the buffer is no longer full.
                System.out.println("Consumer " + Thread.currentThread().getName() + " is signaling the 'notFull' condition. 🔔");
                notFull.signalAll();

            } finally {
                lock.unlock();
            }
            Thread.sleep(100); // Simulate work
        }
    }
}

public class ProducerConsumerWithConditions {
    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        System.out.println("Starting Producer-Consumer with ReentrantLock and Conditions.");

        // Create 2 producers
        executor.submit(() -> {
            try {
                buffer.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        executor.submit(() -> {
            try {
                buffer.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Create 2 consumers
        executor.submit(() -> {
            try {
                buffer.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        executor.submit(() -> {
            try {
                buffer.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // This program will run indefinitely. You need to manually stop it.
    }
}
/*
✅ Expected Output (will run continuously):

Starting Producer-Consumer with ReentrantLock and Conditions.
Producer pool-1-thread-1 produced: 0
Producer pool-1-thread-1 is signaling the 'notEmpty' condition. 🔔
Consumer pool-1-thread-3 consumed: 0
Consumer pool-1-thread-3 is signaling the 'notFull' condition. 🔔
Producer pool-1-thread-2 produced: 1
Producer pool-1-thread-2 is signaling the 'notEmpty' condition. 🔔
...
// When buffer gets full
Buffer is full. Producer pool-1-thread-1 is waiting on 'notFull' condition... 😴
Buffer is full. Producer pool-1-thread-2 is waiting on 'notFull' condition... 😴
Consumer pool-1-thread-4 consumed: 4
Consumer pool-1-thread-4 is signaling the 'notFull' condition. 🔔
// A producer will wake up and continue
Producer pool-1-thread-1 produced: 5
...
*/