public class Solution_VolatileReorderingExample {

    private static int data = 0;
    // THE FIX: Mark the flag as `volatile`.
    // The `volatile` keyword provides two guarantees:
    // 1. VISIBILITY: Changes to `ready` are immediately visible to other threads.
    // 2. ORDERING: Instruction reordering is prevented around this variable.
    //    Specifically, any write that happens *before* a volatile write, is
    //    guaranteed to be visible along with the volatile write.
    private static volatile boolean ready = false;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
                // Busy-wait
            }
            // Because of the happens-before relationship created by volatile,
            // when we read `ready` as true, we are GUARANTEED to see the
            // correct value of `data` (42).
            System.out.println("Data is: " + data);
        }
    }

    public static void main(String[] args) {
        System.out.println("Running the test. It should only print 'Data is: 42'");
        for (int i = 0; i < 10000; i++) {
            data = 0;
            ready = false;

            Thread writerThread = new Thread(() -> {
                // The write to `data` will not be reordered past the volatile write to `ready`.
                data = 42;
                ready = true; // This volatile write acts as a memory barrier.
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
GUARANTEED OUTPUT:
==================
Running the test. It should only print 'Data is: 42'
Data is: 42
Data is: 42
... (10000 times)
Data is: 42
Test finished.

The program will never print "Data is: 0". The `volatile` keyword establishes
a happens-before relationship, ensuring that the write to `data` happens before
the write to `ready`, and the read of `ready` happens before the read of `data`.
*/