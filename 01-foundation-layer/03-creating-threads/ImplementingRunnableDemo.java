// Approach 2: Implementing the Runnable interface
// Ikkada, manam 'pani' (task) ni 'panivadu' (thread) nunchi separate chestunnam.
class MyRunnableTask implements Runnable {
    // 2. Ee `run()` method, mana task logic ni define chestundi.
    @Override
    public void run() {
        System.out.println("Task is executing in thread: " + Thread.currentThread().getName());
    }
}

public class ImplementingRunnableDemo {
    public static void main(String[] args) {
        System.out.println("Main thread started its work.");

        // 1. Main thread, "pani" (task) ni create chestundi.
        MyRunnableTask task = new MyRunnableTask();

        // 3. Main thread, oka kotha worker thread ni create chesi,
        //    daaniki ee task ni assign chestundi.
        Thread worker = new Thread(task, "My-Runnable-Worker");

        // 4. Main thread, worker thread ni start chestundi.
        //    JVM ippudu oka kotha OS thread ni start chesi, task yokka `run()` method ni call chestundi.
        worker.start();

        System.out.println("Main thread finished its work.");
    }
}

/*
Expected Output (The order of the last two lines can vary):
============================================================
Main thread started its work.
Main thread finished its work.
Task is executing in thread: My-Runnable-Worker
*/