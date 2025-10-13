/**
 * 🚀 Visibility Problem - The Solution using 'volatile'
 *
 * `VisibilityProblem.java` lo chusina infinite loop problem ni `volatile` keyword
 * use chesi ela solve cheyalo ee example lo chuddam.
 *
 * The Fix:
 * - `stop` variable ni `volatile` ga declare chestam.
 *   `private volatile boolean stop = false;`
 *
 * How it Works:
 * - `volatile` anedi JMM (Java Memory Model) ki oka direct instruction.
 * - When `main` thread calls `shutDown()` and writes `this.stop = true;`, the `volatile`
 *   keyword guarantees that this change is immediately flushed from the CPU cache to main memory.
 * - When the `Worker` thread checks `while (!stop)`, the `volatile` keyword guarantees
 *   that it will read the value of `stop` directly from main memory, not from its local cache.
 *
 * Ee "read from main memory" and "write to main memory" guarantee valla, `worker` thread
 * `main` thread chesina change ni ventane chustundi, and loop correctly terminate avuthundi.
 *
 * The program will now always exit gracefully.
 */
class FixedWorker extends Thread {
    // THE FIX: 'volatile' keyword tells the JVM that this variable is shared
    // and its value must always be read from/written to main memory.
    private volatile boolean stop = false;

    @Override
    public void run() {
        long counter = 0;
        System.out.println("FixedWorker thread has started and is looping...");
        // Because 'stop' is volatile, the JVM will not cache its value.
        // It will be read from main memory in every iteration.
        while (!stop) {
            counter++;
        }
        // This line is now guaranteed to be printed.
        System.out.println("FixedWorker thread has finished looping after " + counter + " iterations.");
    }

    public void shutDown() {
        System.out.println("Shutdown signal received. Setting volatile 'stop' to true.");
        this.stop = true;
    }
}

public class VisibilitySolution {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting the visibility solution demonstration.");

        FixedWorker workerThread = new FixedWorker();
        workerThread.start();

        Thread.sleep(1000); // 1 second

        workerThread.shutDown();

        workerThread.join(2000); // Wait for a maximum of 2 seconds.

        if (workerThread.isAlive()) {
            System.out.println("-------------------------------------------");
            System.out.println("🔴 FAILURE: This should never happen with volatile!");
            System.out.println("-------------------------------------------");
        } else {
            System.out.println("-------------------------------------------");
            System.out.println("🟢 SUCCESS: The worker thread has terminated correctly.");
            System.out.println("The 'volatile' keyword fixed the visibility problem!");
            System.out.println("-------------------------------------------");
        }
    }
}

/*
✅ Expected Output (Always):

Starting the visibility solution demonstration.
FixedWorker thread has started and is looping...
Shutdown signal received. Setting volatile 'stop' to true.
FixedWorker thread has finished looping after ... iterations.
-------------------------------------------
🟢 SUCCESS: The worker thread has terminated correctly.
The 'volatile' keyword fixed the visibility problem!
-------------------------------------------
*/