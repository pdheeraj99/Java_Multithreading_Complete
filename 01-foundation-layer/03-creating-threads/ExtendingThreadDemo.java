// Approach 1: Extending the Thread class
class MyWorkerThread extends Thread {
    // 2. Ee `run()` method lo unna code, kotha worker thread execute chestundi.
    @Override
    public void run() {
        System.out.println("Worker thread is running. My name is: " + Thread.currentThread().getName());
    }
}

public class ExtendingThreadDemo {
    public static void main(String[] args) {
        System.out.println("Main thread started its work.");

        // 1. Manam, main thread lo, worker thread object ni create chestunnam.
        //    Ippudu worker thread NEW state lo untundi.
        MyWorkerThread worker = new MyWorkerThread();
        worker.setName("My-First-Worker");

        // 3. Main thread, worker thread ni start chestundi.
        //    JVM ippudu oka kotha OS thread ni start chesi, `worker.run()` method ni call chestundi.
        //    Ippudu rendu threads (main and worker) parallel ga run avtunnayi.
        worker.start();

        System.out.println("Main thread finished its work.");
    }
}

/*
Expected Output (The order of the last two lines can vary):
============================================================
Main thread started its work.
Main thread finished its work.
Worker thread is running. My name is: My-First-Worker
*/