public class ThreadPropertiesDemo {
    public static void main(String[] args) throws InterruptedException {

        // Main thread oka chinna task ni define chestundi.
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
             System.out.println(">>> Thread '" + Thread.currentThread().getName() + "' finished.");
        };

        // 1. Main thread oka normal "User Thread" ni create chestundi.
        //    JVM, ee user thread pani poorthi ayye varaku wait chestundi.
        Thread userThread = new Thread(task, "My-User-Worker");

        // 2. Main thread oka "Daemon Thread" ni create chestundi.
        //    Idi oka background service worker lantiది.
        Thread daemonThread = new Thread(task, "My-Daemon-Worker");
        daemonThread.setDaemon(true); // Ee worker ni daemon ga mark chestunnam.

        System.out.println("Main thread is starting both workers...");
        userThread.start();
        daemonThread.start();

        System.out.println("Main thread has finished its work. It will now wait for the user thread to complete.");
        // Main thread ippudu exit avtundi. Kani, `userThread` inka run avtundi kabatti,
        // JVM program ni terminate cheyadu.
        // `userThread` complete ayyaka, inka active user threads emi levu kabatti,
        // `daemonThread` tana pani poorthi cheyakapoina, JVM ventane shutdown aypotundi.
    }
}

/*
Expected Output (The exact interleaving of lines can vary):
============================================================
Main thread is starting both workers...
Main thread has finished its work. It will now wait for the user thread to complete.
Thread 'My-User-Worker' (Daemon: false) is running...
Thread 'My-Daemon-Worker' (Daemon: true) is running...
Thread 'My-User-Worker' (Daemon: false) is running...
Thread 'My-Daemon-Worker' (Daemon: true) is running...
Thread 'My-User-Worker' (Daemon: false) is running...
Thread 'My-Daemon-Worker' (Daemon: true) is running...
>>> Thread 'My-User-Worker' finished.
(Notice that the daemon thread might not get to print its "finished" message, as the JVM exits)
*/