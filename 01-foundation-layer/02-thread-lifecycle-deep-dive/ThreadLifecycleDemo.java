public class ThreadLifecycleDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        // 1. NEW
        Thread thread = new Thread(() -> {
            try {
                // 4. TIMED_WAITING
                System.out.println("Thread is going to sleep. State: " + Thread.currentThread().getState());
                Thread.sleep(2000);

                // 5. BLOCKED
                synchronized (lock) {
                    // This block is to demonstrate the BLOCKED state
                    System.out.println("Thread acquired the lock.");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 6. TERMINATED (after run() completes)
        });

        System.out.println("After creation. State: " + thread.getState()); // NEW

        // 2. RUNNABLE
        thread.start();
        System.out.println("After start(). State: " + thread.getState()); // RUNNABLE

        // Give the thread a moment to start and go to sleep
        Thread.sleep(500);
        System.out.println("While thread is sleeping. State: " + thread.getState()); // TIMED_WAITING

        // Main thread acquires the lock first
        synchronized (lock) {
            // Give the thread time to finish sleeping and try to acquire the lock
            Thread.sleep(2000);
            System.out.println("While thread is waiting for lock. State: " + thread.getState()); // BLOCKED
        }

        // Wait for the thread to finish its execution
        thread.join();
        System.out.println("After thread has finished. State: " + thread.getState()); // TERMINATED
    }
}