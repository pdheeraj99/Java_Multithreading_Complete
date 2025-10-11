public class Problem_ReorderingExample {

    private static int data = 0;
    private static boolean ready = false;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            // Wait until the flag is set
            while (!ready) {
                // Busy-wait
            }
            // Read the data
            System.out.println("Data is: " + data);
        }
    }

    public static void main(String[] args) {
        // This loop will run the test many times to increase the chance
        // of observing the reordering.
        for (int i = 0; i < 10000; i++) {
            data = 0;
            ready = false;

            Thread writerThread = new Thread(() -> {
                // These two lines can be reordered by the compiler or the CPU
                // because they don't have any data dependency on each other
                // from a single-threaded perspective.
                data = 42;
                ready = true;
            });

            ReaderThread readerThread = new ReaderThread();

            writerThread.start();
            readerThread.start();

            try {
                writerThread.join();
                readerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Test finished.");
    }
}

/*
EXPECTED BEHAVIOR (what we want):
=================================
The program should ONLY print "Data is: 42" every time.

ACTUAL BEHAVIOR (what might happen):
====================================
Most of the time, it will print "Data is: 42".
But occasionally, you will see it print "Data is: 0".

Why?
Because the writer thread might have its instructions reordered.
The line `ready = true;` might be executed BEFORE `data = 42;`.
If the reader thread checks the `ready` flag right after it becomes true
but before `data` is set, it will read the old value of `data`, which is 0.
This is a classic data race caused by instruction reordering.
*/