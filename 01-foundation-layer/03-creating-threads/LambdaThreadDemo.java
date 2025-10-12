public class LambdaThreadDemo {
    public static void main(String[] args) {
        System.out.println("Main thread started its work.");

        // `Runnable` anedi oka functional interface kabatti, manam daani kosam
        // separate class rayakkarledu. Main thread direct ga oka lambda expression
        // roopam lo task ni define chestundi.
        Runnable task = () -> {
            System.out.println("Lambda task is executing in thread: " + Thread.currentThread().getName());
        };

        // Main thread, oka kotha worker thread ni create chesi, daaniki ee task ni isthundi.
        Thread worker = new Thread(task, "My-Lambda-Worker");

        // Main thread, worker thread ni start chestundi.
        worker.start();

        System.out.println("Main thread finished its work.");
    }
}

/*
Expected Output (The order of the last two lines can vary):
============================================================
Main thread started its work.
Main thread finished its work.
Lambda task is executing in thread: My-Lambda-Worker
*/