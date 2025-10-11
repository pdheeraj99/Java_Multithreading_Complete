public class Problem_VisibilityExample {

    // A shared flag. Ee variable ni `volatile` ga mark cheyaledu.
    private static boolean stopRequested = false;

    public static void main(String[] args) throws InterruptedException {

        // The "reader" thread
        // Ee thread, `stopRequested` flag `true` ayye varaku loop lo tirugutu untundi.
        Thread readerThread = new Thread(() -> {
            System.out.println("Reader thread started. Waiting for stop signal...");
            while (!stopRequested) {
                // This is a "busy-wait" loop. It continuously checks the flag.
                // JIT compiler ee loop ni chusi, "stopRequested eppudu maradu" ani
                // anukuni, daanini optimize chesi, `while(true)` laaga marcheyochu.
                // Leda, `stopRequested` yokka value ni cache lo ne unchi,
                // main memory lo marina kotha value ni chudakapovachu.
            }
            System.out.println("Reader thread received stop signal. Stopping.");
        });

        readerThread.start();

        // Main thread kontha sepu aagi...
        Thread.sleep(1000);

        // The "writer" thread (in this case, the main thread)
        // `stopRequested` flag ni `true` ga marustundi.
        System.out.println("Main thread is requesting stop.");
        stopRequested = true;
        System.out.println("Stop signal sent.");

        // Wait for the reader thread to finish.
        readerThread.join();
        System.out.println("Main thread finished.");
    }
}

/*
EXPECTED BEHAVIOR (what we want):
=================================
Reader thread started. Waiting for stop signal...
Main thread is requesting stop.
Stop signal sent.
Reader thread received stop signal. Stopping.
Main thread finished.

ACTUAL BEHAVIOR (what might happen):
====================================
Reader thread started. Waiting for stop signal...
Main thread is requesting stop.
Stop signal sent.
... and the program hangs forever!

The reader thread might never see the updated value of `stopRequested`
because of caching or JIT optimizations. It's stuck in an infinite loop.
This is a classic visibility problem.
*/