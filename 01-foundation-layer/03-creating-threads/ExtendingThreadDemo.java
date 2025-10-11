// Approach 1: Extending the Thread class
// Ikkada, mana class eh oka Thread.
class MyWorkerThread extends Thread {

    // 1. run() method ni override cheyali. Ee method lo unna code eh
    //    kotha thread lo execute avtundi.
    @Override
    public void run() {
        System.out.println("Hello from MyWorkerThread! My name is: " + Thread.currentThread().getName());
    }
}

public class ExtendingThreadDemo {
    public static void main(String[] args) {
        System.out.println("Main thread started. Current thread: " + Thread.currentThread().getName());

        // 2. Manam create chesina thread class ki object ni create cheyali.
        //    Ippudu thread NEW state lo untundi.
        MyWorkerThread thread = new MyWorkerThread();

        // 3. Debugging kosam thread ki peru pettadam manchi practice.
        thread.setName("My-First-Thread");

        // 4. thread.start() method ni call cheyali. Idi chala important.
        //    Idi JVM ki oka kotha OS thread ni create chesi, daaniki ee `run()`
        //    method ni assign cheyamani cheptundi. Thread RUNNABLE state loki veltundi.
        //    Direct ga thread.run() call cheyakudadhu! Ala cheste, adi normal method
        //    call laga main thread lo ne execute avtundi.
        thread.start();

        System.out.println("Main thread finished. Current thread: " + Thread.currentThread().getName());
    }
}

/*
Expected Output (The order of the last two lines can vary):
============================================================
Main thread started. Current thread: main
Main thread finished. Current thread: main
Hello from MyWorkerThread! My name is: My-First-Thread
*/