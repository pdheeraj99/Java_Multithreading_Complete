public class LambdaThreadDemo {
    public static void main(String[] args) {
        System.out.println("Main thread started. Current thread: " + Thread.currentThread().getName());

        // Approach 3: Using a Lambda expression (Java 8+)
        // `Runnable` anedi oka functional interface kabatti (okate abstract method undi),
        // manam daani kosam separate class rayakkarledu.
        // Ee lambda expression, `Runnable` yokka `run()` method ki implementation.
        Runnable task = () -> {
            System.out.println("Hello from a Lambda Thread! Executing in thread: " + Thread.currentThread().getName());
        };

        // Thread ni create chesi, task ni pass cheyadam same as before.
        Thread thread = new Thread(task, "My-Lambda-Thread"); // We can also set the name in the constructor
        thread.start();

        System.out.println("Main thread finished. Current thread: " + Thread.currentThread().getName());
    }
}

/*
Expected Output (The order of the last two lines can vary):
============================================================
Main thread started. Current thread: main
Main thread finished. Current thread: main
Hello from a Lambda Thread! Executing in thread: My-Lambda-Thread
*/