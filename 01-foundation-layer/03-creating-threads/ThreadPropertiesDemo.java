public class ThreadPropertiesDemo {
    public static void main(String[] args) throws InterruptedException {

        // A simple task that prints its name and runs for a while.
        Runnable task = () -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("Thread '" + Thread.currentThread().getName() +
                                   "' (Daemon: " + Thread.currentThread().isDaemon() + ") is running...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
             System.out.println("Thread '" + Thread.currentThread().getName() + "' finished.");
        };

        // 1. User Thread (Default)
        // Evi foreground threads. JVM, okka user thread run avthunna kuda exit avvadu.
        Thread userThread = new Thread(task);
        userThread.setName("My-User-Worker");

        // 2. Daemon Thread
        // Evi background threads. Anni user threads complete ayipothe,
        // JVM daemon threads kosam wait cheyakunda exit aypotundi.
        Thread daemonThread = new Thread(task);
        daemonThread.setName("My-Daemon-Worker");
        daemonThread.setDaemon(true); // Mark this as a daemon thread. Must be done before start().

        System.out.println("Starting threads...");
        userThread.start();
        daemonThread.start();

        // userThread.join(); // Ee line uncomment cheste, main thread userThread kosam wait chestundi.

        System.out.println("Main thread has finished its work.");
        // Main thread ippudu exit avtundi. Kani, `userThread` inka run avtundi kabatti,
        // JVM program ni terminate cheyadu.
        // `userThread` complete ayyaka, inka active user threads emi levu kabatti,
        // `daemonThread` run avthunna kuda JVM ventane shutdown aypotundi.
    }
}

/*
Expected Output (The exact interleaving of lines can vary):
============================================================
Starting threads...
Main thread has finished its work.
Thread 'My-User-Worker' (Daemon: false) is running...
Thread 'My-Daemon-Worker' (Daemon: true) is running...
Thread 'My-User-Worker' (Daemon: false) is running...
Thread 'My-Daemon-Worker' (Daemon: true) is running...
Thread 'My-User-Worker' (Daemon: false) is running...
Thread 'My-Daemon-Worker' (Daemon: true) is running...
Thread 'My-User-Worker' finished.
(Notice that the daemon thread might not get to print its "finished" message)
*/