import java.util.LinkedList;
import java.util.Queue;

/**
 * 🚀 Producer-Consumer - The Correct Solution with `wait()` and `notifyAll()`
 *
 * Ee example lo, manam `wait()` and `notifyAll()` ni use chesi Producer-Consumer
 * problem ni correctly solve cheddam.
 *
 * How it Works:
 * - **Producer**: Buffer full ga unte, `wait()` call chesi lock ni release chesi padukuntundi.
 *   It no longer holds the lock while waiting. Item add chesaka, `notifyAll()` tho
 *   sleeping consumers ni leputundi.
 * - **Consumer**: Buffer empty ga unte, `wait()` call chesi lock ni release chesi padukuntundi.
 *   Item teesukunna tarvata, `notifyAll()` tho sleeping producers ni leputundi.
 *
 * This approach is:
 * - **Efficient**: No CPU cycles are wasted in busy-waiting. Threads go to sleep.
 * - **Deadlock-Free**: Threads release the lock before waiting, allowing other threads
 *   to acquire the lock and change the buffer's state.
 *
 * This program will run smoothly without hanging.
 */
public class ProducerConsumerSolution {
    private final Queue<Integer> buffer = new LinkedList<>();
    private final int capacity = 5;
    private int value = 0;

    public void produce() throws InterruptedException {
        while (true) {
            synchronized (this) {
                // GOOD: Use a while loop to wait for the condition.
                // This handles spurious wakeups.
                while (buffer.size() == capacity) {
                    System.out.println("Buffer is full. Producer is releasing the lock and waiting... 😴");
                    wait(); // Releases the lock and goes to WAITING state.
                    System.out.println("Producer woke up and re-acquired the lock! Checking condition again...");
                }

                System.out.println("Producer produced: " + value);
                buffer.add(value++);

                // Notify all waiting threads (specifically, consumers) that the state has changed.
                System.out.println("Producer is notifying other threads... 🔔");
                notifyAll();
            }
            Thread.sleep(50); // Simulate work outside the synchronized block
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                // GOOD: Use a while loop to wait for the condition.
                while (buffer.isEmpty()) {
                    System.out.println("Buffer is empty. Consumer is releasing the lock and waiting... 😴");
                    wait(); // Releases the lock and waits.
                    System.out.println("Consumer woke up and re-acquired the lock! Checking condition again...");
                }

                int consumedValue = buffer.poll();
                System.out.println("Consumer consumed: " + consumedValue);

                // Notify all waiting threads (specifically, producers) that the state has changed.
                System.out.println("Consumer is notifying other threads... 🔔");
                notifyAll();
            }
            Thread.sleep(100); // Simulate work outside the synchronized block
        }
    }

    public static void main(String[] args) {
        ProducerConsumerSolution solution = new ProducerConsumerSolution();

        Thread producerThread = new Thread(() -> {
            try {
                solution.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                solution.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("Starting the CORRECT producer-consumer. This will run smoothly.");
        producerThread.start();
        consumerThread.start();
    }
}
/*
✅ Expected Output (will run continuously):

Starting the CORRECT producer-consumer. This will run smoothly.
Producer produced: 0
Producer is notifying other threads... 🔔
Consumer consumed: 0
Consumer is notifying other threads... 🔔
Producer produced: 1
Producer is notifying other threads... 🔔
...
...
// When buffer gets full
Producer produced: 4
Producer is notifying other threads... 🔔
Buffer is full. Producer is releasing the lock and waiting... 😴
Consumer consumed: 1
Consumer is notifying other threads... 🔔
Producer woke up and re-acquired the lock! Checking condition again...
Producer produced: 5
Producer is notifying other threads... 🔔
... and so on.
*/