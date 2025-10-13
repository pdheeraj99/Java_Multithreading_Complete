/**
 * 🚀 Visibility Problem - Live Demonstration
 *
 * Ee example lo, manam JMM rules ni follow avvakapothe vachhe "Visibility Problem" ni
 * live ga choodataniki try chestam.
 *
 * Scenario:
 * - Oka `Worker` thread undi, adi `stop` flag `false` ga unnantha varaku loop tirugutune untundi.
 * - `main` thread kontha sepu aagi, `stop` flag ni `true` ga set chestundi.
 *
 * Problem:
 * - `stop` variable anedi plain, non-volatile boolean.
 * - The `worker` thread, performance kosam, `stop` yokka value ni tana CPU core cache lo store chesukovachu.
 * - `main` thread `stop` ni `true` ga set chesina, aa change main memory ki write avuthundi.
 * - Kaani `worker` thread tana local cache lone chustu undatam valla, daaniki ee change eppatiki
 *   kanipinchakapovachu. Result? An infinite loop!
 *
 * NOTE: Ee problem 100% of the time reproduce avvalani ledu. It depends on the JVM, hardware,
 * and other runtime factors. Kaani, idi a very real and dangerous bug.
 */
class Worker extends Thread {
    // `volatile` keyword lekunda ee variable ni declare cheddam. Idhe asalu problem.
    // To fix this, you would declare it as: private volatile boolean stop = false;
    private boolean stop = false;

    @Override
    public void run() {
        long counter = 0;
        System.out.println("Worker thread has started and is looping...");
        // The JVM might optimize this by reading `stop` only once.
        while (!stop) {
            // This is a "busy-wait" loop. It keeps the CPU core very busy.
            // Ee loop lo emaina pani unte, for example `System.out.println`,
            // adi problem ni "mask" cheyochu, endukante `println` is synchronized
            // and creates a happens-before relationship, flushing the cache.
            // So, manam ikkada emi pettakudadu to see the raw problem.
            counter++;
        }
        // Ee line eppatiki print avvakapovachu!
        System.out.println("Worker thread has finished looping after " + counter + " iterations.");
    }

    public void shutDown() {
        System.out.println("Shutdown signal received. Setting stop to true.");
        this.stop = true;
    }
}


public class VisibilityProblemExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting the visibility problem demonstration.");

        Worker workerThread = new Worker();
        workerThread.start();

        // Main thread kontha sepu wait cheddam.
        // Ee time lo worker thread full speed lo loop tirugutundi.
        Thread.sleep(1000); // 1 second

        // Ippudu worker ni aapaamani signal pampudham.
        workerThread.shutDown();

        // Worker thread aagindo ledo choodataniki kontha time iddam.
        // `join(timeout)` will wait for the specified time for the thread to die.
        workerThread.join(2000); // Wait for a maximum of 2 seconds.

        if (workerThread.isAlive()) {
            System.out.println("-------------------------------------------");
            System.out.println("🔴 FAILURE: The worker thread is still alive!");
            System.out.println("It did not see the 'stop=true' change. This is a visibility problem.");
            System.out.println("The program will now hang forever... Press Ctrl+C to exit.");
            System.out.println("-------------------------------------------");
        } else {
            System.out.println("-------------------------------------------");
            System.out.println("🟢 SUCCESS: The worker thread has terminated correctly.");
            System.out.println("This time, the change was visible. But you can't rely on this!");
            System.out.println("-------------------------------------------");
        }
    }
}

/*
✅ Expected Output (Most of the time on modern JVMs, but not guaranteed):

Starting the visibility problem demonstration.
Worker thread has started and is looping...
Shutdown signal received. Setting stop to true.
-------------------------------------------
🔴 FAILURE: The worker thread is still alive!
It did not see the 'stop=true' change. This is a visibility problem.
The program will now hang forever... Press Ctrl+C to exit.
-------------------------------------------

How to fix it?
Change `private boolean stop = false;`
to     `private volatile boolean stop = false;`

With `volatile`, the output will always be:
Starting the visibility problem demonstration.
Worker thread has started and is looping...
Shutdown signal received. Setting stop to true.
Worker thread has finished looping after ... iterations.
-------------------------------------------
🟢 SUCCESS: The worker thread has terminated correctly.
...
-------------------------------------------
*/