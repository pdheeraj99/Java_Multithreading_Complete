import java.util.LinkedList;
import java.util.Queue;

/**
 * 🚀 Producer-Consumer - The BROKEN Version
 *
 * Ee example lo, manam Producer-Consumer problem ni `wait()` and `notify()` lekunda
 * solve cheyadaniki try cheddam. We will use a "busy-wait" approach.
 *
 * The Problem with this approach:
 * 1.  **Deadlock Potential**: The `produce` and `consume` methods are `synchronized`.
 *     - If the buffer is full, the Producer holds the lock and loops forever, waiting for space.
 *       The Consumer can't get the lock to *make* space. -> DEADLOCK!
 *     - If the buffer is empty, the Consumer holds the lock and loops forever, waiting for items.
 *       The Producer can't get the lock to *add* items. -> DEADLOCK!
 * 2.  **CPU Waste**: Even if it doesn't deadlock, the thread that is waiting is spinning in a
 *     tight `while` loop, consuming 100% of its CPU core doing absolutely nothing useful.
 *
 * This program will likely hang and you will have to manually terminate it.
 * This demonstrates why busy-waiting on a condition while holding a lock is a terrible idea.
 */
public class ProducerConsumerProblem {
    // The shared buffer with a fixed size
    private final Queue<Integer> buffer = new LinkedList<>();
    private final int capacity = 5;
    private int value = 0;

    public void produce() throws InterruptedException {
        while (true) {
            synchronized (this) {
                // BAD: Busy-wait while the buffer is full
                while (buffer.size() == capacity) {
                    // This thread holds the lock and spins, preventing the consumer from taking items.
                    // System.out.println("Buffer is full, producer is waiting..."); // Uncomment to see the spin
                }

                System.out.println("Producer produced: " + value);
                buffer.add(value++);
            }
            Thread.sleep(50); // Simulate some work
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                // BAD: Busy-wait while the buffer is empty
                while (buffer.isEmpty()) {
                    // This thread holds the lock and spins, preventing the producer from adding items.
                    // System.out.println("Buffer is empty, consumer is waiting..."); // Uncomment to see the spin
                }

                int consumedValue = buffer.poll();
                System.out.println("Consumer consumed: " + consumedValue);
            }
            Thread.sleep(100); // Simulate some work
        }
    }

    public static void main(String[] args) {
        ProducerConsumerProblem problem = new ProducerConsumerProblem();

        Thread producerThread = new Thread(() -> {
            try {
                problem.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                problem.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("Starting the BROKEN producer-consumer. This will likely hang.");
        producerThread.start();
        consumerThread.start();
    }
}

/*
✅ Expected Output:
The program will print a few lines and then hang. It will stop making progress
once the buffer becomes full, as the producer will hold the lock and spin,
and the consumer will be unable to acquire the lock to consume.

Starting the BROKEN producer-consumer. This will likely hang.
Producer produced: 0
Producer produced: 1
Producer produced: 2
Producer produced: 3
Producer produced: 4
Consumer consumed: 0
Consumer consumed: 1
Consumer consumed: 2
Consumer consumed: 3
Consumer consumed: 4
Producer produced: 5
... (and then it will hang) ...
*/