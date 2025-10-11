public class DataRaceExample {

    private static int value = 0;
    private static boolean flag = false;

    // Thread 1 will run this task
    private static void writer() {
        value = 42;  // Write to the shared variable
        flag = true; // Set the flag
    }

    // Thread 2 will run this task
    private static void reader() {
        if (flag) {
            // We expect to see 42 here, but is it guaranteed?
            System.out.println("Flag is true, value is: " + value);
        } else {
            System.out.println("Flag is false");
        }
    }

    public static void main(String[] args) {
        // Run this example multiple times. You might see different outputs!
        // This is because there is no "happens-before" relationship between
        // the write to 'flag' in the writer thread and the read of 'flag'
        // in the reader thread.

        // The JIT compiler and the CPU are free to reorder the instructions in the writer method.
        // It's possible that `flag = true` is executed BEFORE `value = 42`.

        // If that happens, the reader thread might see `flag` as true,
        // but `value` as still 0. This is a "data race".

        Thread writerThread = new Thread(DataRaceExample::writer);
        Thread readerThread = new Thread(DataRaceExample::reader);

        writerThread.start();
        readerThread.start();

        try {
            writerThread.join();
            readerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/*
Possible Outputs (due to data race):
====================================
1. Ideal (and common) case:
   Flag is true, value is: 42

2. Reordering case (the data race!):
   Flag is true, value is: 0

3. Visibility case (less common on modern CPUs, but possible):
   Flag is false
   (The writer thread finished, but its changes were not yet visible to the reader thread)

How to fix this? Make the `flag` variable `volatile`.
`private static volatile boolean flag = false;`
This establishes a happens-before relationship. The write to the volatile `flag`
happens-before any subsequent read of that same `flag`. This guarantees that
the write to `value` is also visible.
*/