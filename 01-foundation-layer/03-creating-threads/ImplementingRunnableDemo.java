// Approach 2: Implementing the Runnable interface
// Ikkada, manam 'pani' (task) ni 'panivadu' (thread) nunchi separate chestunnam.
// MyRunnableTask anedi pani, Thread anedi panivadu.
class MyRunnableTask implements Runnable {

    // 1. Runnable interface ni implement cheyali. Idi oka functional interface.
    //    Daaniki `run()` ane okate abstract method untundi.
    @Override
    public void run() {
        // 2. run() method lo mana task logic ni rayali.
        System.out.println("Hello from MyRunnableTask! Executing in thread: " + Thread.currentThread().getName());
    }
}

public class ImplementingRunnableDemo {
    public static void main(String[] args) {
        System.out.println("Main thread started. Current thread: " + Thread.currentThread().getName());

        // 3. Task object ni create cheyali. Idi pani, worker kaadu.
        MyRunnableTask task = new MyRunnableTask();

        // 4. Thread object ni create chesi, constructor lo mana task ni pass cheyali.
        //    Ippudu, ee thread ki emi cheyalo telusu.
        Thread thread = new Thread(task);
        thread.setName("My-Runnable-Worker");

        // 5. Thread ni start cheyali.
        thread.start();

        System.out.println("Main thread finished. Current thread: " + Thread.currentThread().getName());
    }
}

/*
Expected Output (The order of the last two lines can vary):
============================================================
Main thread started. Current thread: main
Main thread finished. Current thread: main
Hello from MyRunnableTask! Executing in thread: My-Runnable-Worker
*/