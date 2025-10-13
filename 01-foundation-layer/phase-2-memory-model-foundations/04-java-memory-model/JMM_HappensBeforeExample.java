/**
 * 🚀 Java Memory Model (JMM) - Happens-Before Guarantee in Action.
 *
 * Ee example lo, manam `synchronized` block yokka "Monitor Lock Rule" ni demonstrate chestam.
 * Rule: An unlock on a monitor happens-before every subsequent lock on the same monitor.
 *
 * Scenario:
 * 1. Manam `sharedValue` ane oka variable ni create chestam.
 * 2. Oka "writer" thread (T1) ee `sharedValue` ni update chesi, `isReady` flag ni `true` ga set chestundi.
 *    Ee rendu actions `synchronized` block lo jarugutayi.
 * 3. Oka "reader" thread (T2) `isReady` flag `true` ayye varaku wait chestundi.
 * 4. `isReady` `true` avvagane, T2 `sharedValue` ni read chestundi.
 *
 * JMM Guarantee:
 * T1 `synchronized` block ni exit chesinappudu (unlock), `sharedValue = 100` and `isReady = true`
 * ane writes anni memory ki commit avuthayi. T2 `synchronized` block loki enter ayinappudu (lock),
 * aa writes anni T2 ki visible avuthayi ani JMM guarantee istundi.
 *
 * So, T2 eppudu `isReady` ni `true` ga chusina, daaniki `sharedValue` kuda `100` ga kanipinchali.
 * It will never see `sharedValue` as `0` after seeing `isReady` as `true`.
 */
class SharedResource {
    // These variables are shared between threads.
    private Object value = null;
    private boolean isReady = false;
    private final Object lock = new Object(); // The monitor lock

    // This method is called by the Writer-Thread
    public void write() {
        // Synchronizing on the 'lock' object
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " has acquired the lock for writing.");
            value = "Hello World! 🌍";
            isReady = true;
            System.out.println(Thread.currentThread().getName() + " has updated the value and is about to release the lock.");
        } // Unlock happens here. All writes inside are flushed to main memory.
    }

    // This method is called by the Reader-Thread
    public void read() {
        // Synchronizing on the SAME 'lock' object is crucial!
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " has acquired the lock for reading.");
            // Because of the happens-before relationship, if we see isReady = true,
            // we are GUARANTEED to see the correct value of 'value'.
            if (isReady) {
                System.out.println("🎉 SUCCESS: Reader thread sees the correct value: '" + value + "'");
            } else {
                // This block should ideally not be reached if the writer runs first.
                System.out.println("😞 FAILURE: Reader thread sees isReady=false. Value is: '" + value + "'");
            }
        } // Unlock happens here
    }
}

public class JMM_HappensBeforeExample {
    public static void main(String[] args) throws InterruptedException {
        SharedResource resource = new SharedResource();

        // Writer Thread
        Thread writerThread = new Thread(() -> {
            // Wait for a moment to ensure the reader starts first
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            resource.write();
        }, "Writer-Thread");

        // Reader Thread
        Thread readerThread = new Thread(() -> {
            // Give writer a chance to start and acquire lock first
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            resource.read();
        }, "Reader-Thread");

        System.out.println("Starting both threads. Let's see the JMM guarantee in action.");

        writerThread.start();
        readerThread.start();

        writerThread.join();
        readerThread.join();

        System.out.println("Both threads have finished their execution.");
    }
}

/*
✅ Expected Output:

Starting both threads. Let's see the JMM guarantee in action.
Writer-Thread has acquired the lock for writing.
Writer-Thread has has updated the value and is about to release the lock.
Reader-Thread has acquired the lock for reading.
🎉 SUCCESS: Reader thread sees the correct value: 'Hello World! 🌍'
Both threads have finished their execution.
*/