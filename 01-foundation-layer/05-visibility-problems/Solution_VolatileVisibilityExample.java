public class Solution_VolatileVisibilityExample {

    // THE FIX: Mark the shared flag as `volatile`.
    // Ee `volatile` keyword, JMM ki oka instruction istundi:
    // 1. Ee variable ki write chesinappudu, ventane main memory ki flush chey. (Store Barrier)
    // 2. Ee variable ni read chesinappudu, eppudu main memory nunchi chudu. (Load Barrier)
    // Idi `stopRequested` yokka latest value anni threads ki kanipistundi ani guarantee istundi.
    private static volatile boolean stopRequested = false;

    public static void main(String[] args) throws InterruptedException {

        // The "reader" thread
        Thread readerThread = new Thread(() -> {
            System.out.println("Reader thread started. Waiting for stop signal...");
            while (!stopRequested) {
                // `volatile` read valla, ee loop eppudu `stopRequested` yokka
                // latest value ni main memory nunchi chustundi.
            }
            System.out.println("Reader thread received stop signal. Stopping.");
        });

        readerThread.start();

        Thread.sleep(1000);

        // The "writer" thread (main thread)
        System.out.println("Main thread is requesting stop.");
        stopRequested = true; // `volatile` write
        System.out.println("Stop signal sent.");

        // Wait for the reader thread to finish.
        readerThread.join();
        System.out.println("Main thread finished.");
    }
}

/*
GUARANTEED OUTPUT (with volatile):
==================================
Reader thread started. Waiting for stop signal...
Main thread is requesting stop.
Stop signal sent.
Reader thread received stop signal. Stopping.
Main thread finished.

Because `volatile` establishes a happens-before relationship between the write
in the main thread and the read in the reader thread, the program is now
guaranteed to terminate correctly.
*/