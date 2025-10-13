/**
 * 🚀 Thread Creation - Method 1: Extending the Thread Class.
 *
 * Ee approach lo, manam `java.lang.Thread` class ni direct ga extend chestam.
 * Idi thread ni create cheyadaniki most basic way.
 *
 * Pros:
 * - Simple and easy for basic use cases.
 * - Thread-specific methods ni direct ga access cheyochu (e.g., using `this`).
 *
 * Cons:
 * - Java supports only single inheritance. If you extend `Thread`, you cannot extend any other class.
 *   Idi pedda limitation.
 * - Violates "Separation of Concerns" principle. The "task" (what to run) is tightly coupled with
 *   the "runner" (the thread itself).
 */
class MyThread extends Thread {

    private int threadNumber;

    public MyThread(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    // `run()` anedi Thread class lo unna method. Manam daanini override chestunnam.
    // Ee method lo unna code, thread start ayinappudu execute avuthundi.
    // This is the heart of the thread! ❤️
    @Override
    public void run() {
        System.out.println("🚀 Chef " + threadNumber + " (Thread) is starting its work!");

        // Let's simulate some work
        for (int i = 1; i <= 5; i++) {
            System.out.println("Chef " + threadNumber + " is cooking item " + i + " using Thread: " + Thread.currentThread().getName());
            try {
                // Konchem time pause cheddam, to simulate a real-world task
                // and to allow other threads to run.
                Thread.sleep(1000); // 1000 milliseconds = 1 second
            } catch (InterruptedException e) {
                // Thread.sleep() can be interrupted by another thread.
                // Appudu ee exception vasthundi. Manam daanini handle cheyali.
                System.err.println("Chef " + threadNumber + " was interrupted! 😮");
                // Restore the interrupted status
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("✅ Chef " + threadNumber + " has finished all its tasks.");
    }
}

public class T01_UsingThreadClass {

    public static void main(String[] args) {
        System.out.println("Restaurant Manager (Main Thread) is opening the kitchen.");

        // Manam create chesina `MyThread` class ki objects create cheddam.
        // Ivi ippudu NEW state lo untayi.
        MyThread chef1 = new MyThread(1);
        chef1.setName("Chef-Anand"); // Thread ki oka peru ivvadam good practice for debugging.

        MyThread chef2 = new MyThread(2);
        chef2.setName("Chef-Bhavani");

        // IMPORTANT!
        // `start()` method ni call cheste ne kottha thread create ayyi, `run()` method execution start avuthundi.
        // Meeru direct ga `chef1.run()` ani call cheste, adi normal method call laaga
        // main thread lo ne execute avuthundi, kottha thread create avvadu.
        // So, ALWAYS use start() to begin a new thread's execution.
        System.out.println("Manager is asking the chefs to start working...");
        chef1.start(); // This moves the thread from NEW to RUNNABLE state.
        chef2.start(); // This moves the second thread from NEW to RUNNABLE state.

        System.out.println("Manager has assigned tasks to both chefs and is now waiting for them to finish.");
        // Main thread will continue its own execution.
        // In this case, it just prints the above line and exits.
        // The JVM will not shut down until all non-daemon threads (like our chef threads) are finished.
    }
}

/*
✅ Expected Output (Order of cooking items might vary):

Restaurant Manager (Main Thread) is opening the kitchen.
Manager is asking the chefs to start working...
Manager has assigned tasks to both chefs and is now waiting for them to finish.
🚀 Chef 1 (Thread) is starting its work!
Chef 1 is cooking item 1 using Thread: Chef-Anand
🚀 Chef 2 (Thread) is starting its work!
Chef 2 is cooking item 1 using Thread: Chef-Bhavani
Chef 2 is cooking item 2 using Thread: Chef-Bhavani
Chef 1 is cooking item 2 using Thread: Chef-Anand
Chef 2 is cooking item 3 using Thread: Chef-Bhavani
Chef 1 is cooking item 3 using Thread: Chef-Anand
Chef 1 is cooking item 4 using Thread: Chef-Anand
Chef 2 is cooking item 4 using Thread: Chef-Bhavani
Chef 1 is cooking item 5 using Thread: Chef-Anand
✅ Chef 1 has finished all its tasks.
Chef 2 is cooking item 5 using Thread: Chef-Bhavani
✅ Chef 2 has finished all its tasks.
*/