import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 🚀 The ABA Problem - Live Demonstration and Solution
 *
 * Ee example lo, manam ABA problem ni chuddam and daanini `AtomicStampedReference`
 * tho ela solve cheyalo kuda chuddam.
 *
 * Scenario:
 * - Oka shared resource undi (oka simple String object).
 * - Thread-1 ee resource ni chusi, daanini update cheyalani decide avuthundi.
 * - But before it can update, Thread-2 vachi, aa resource ni A -> B ki,
 *   malli B -> A ki marchesindi.
 * - Ippudu Thread-1 vachi chusthe, value inka A ga ne undi. A simple CAS will succeed,
 *   which is wrong because an intermediate change was missed.
 */
public class ABAProblemAndSolution {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 1. Demonstrating the ABA Problem with AtomicReference ---");
        demonstrateABAProblem();

        Thread.sleep(1000); // Pause between demonstrations

        System.out.println("\n\n--- 2. Demonstrating the Solution with AtomicStampedReference ---");
        demonstrateABASolution();
    }

    /**
     * This method shows how a simple AtomicReference can be fooled by the ABA problem.
     */
    public static void demonstrateABAProblem() throws InterruptedException {
        // Initial value is "A"
        final AtomicReference<String> ref = new AtomicReference<>("A");
        System.out.println("Initial value: " + ref.get());

        // Thread-1 reads the initial value and plans to change it to "C".
        Thread thread1 = new Thread(() -> {
            String initialValue = ref.get(); // Reads "A"
            System.out.println("Thread-1 read the value: " + initialValue);

            // Simulate some delay before T1 tries to update
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            // Try to change from "A" to "C".
            // This CAS will succeed, even though the value was changed in the meantime.
            boolean success = ref.compareAndSet(initialValue, "C");
            System.out.println("Thread-1 CAS from 'A' to 'C' was successful? " + success);
            if (success) {
                System.out.println("🔴 FAILURE: Thread-1 was fooled by the ABA problem!");
            }
        });

        // Thread-2 comes in and quickly changes A -> B -> A.
        Thread thread2 = new Thread(() -> {
            // Change A -> B
            ref.compareAndSet("A", "B");
            System.out.println("Thread-2 changed value to: " + ref.get());
            // Change B -> A
            ref.compareAndSet("B", "A");
            System.out.println("Thread-2 changed value back to: " + ref.get());
        });

        thread1.start();
        // Give T1 a moment to read the initial value
        try { Thread.sleep(50); } catch (InterruptedException e) {}
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("Final value: " + ref.get());
    }

    /**
     * This method shows how AtomicStampedReference solves the ABA problem.
     */
    public static void demonstrateABASolution() throws InterruptedException {
        // Initial value is "A" with a stamp (version) of 0.
        final AtomicStampedReference<String> stampedRef = new AtomicStampedReference<>("A", 0);
        System.out.println("Initial value: " + stampedRef.getReference() + ", Initial stamp: " + stampedRef.getStamp());

        // Thread-3 reads the initial value and stamp.
        Thread thread3 = new Thread(() -> {
            int[] stampHolder = new int[1];
            String initialValue = stampedRef.get(stampHolder);
            int initialStamp = stampHolder[0];
            System.out.println("Thread-3 read value: " + initialValue + ", stamp: " + initialStamp);

            try { Thread.sleep(500); } catch (InterruptedException e) {}

            // Try to change from "A" to "C", but only if the stamp is also unchanged.
            // This CAS will FAIL because T4 will have changed the stamp.
            boolean success = stampedRef.compareAndSet(initialValue, "C", initialStamp, initialStamp + 1);
            System.out.println("Thread-3 CAS from 'A' (stamp 0) to 'C' was successful? " + success);
            if (!success) {
                System.out.println("🟢 SUCCESS: Thread-3 correctly detected the intermediate change (the stamp mismatch)!");
            }
        });

        // Thread-4 comes in and quickly changes A -> B -> A, incrementing the stamp each time.
        Thread thread4 = new Thread(() -> {
            int stamp = stampedRef.getStamp();
            // Change A (stamp 0) -> B (stamp 1)
            stampedRef.compareAndSet("A", "B", stamp, stamp + 1);
            System.out.println("Thread-4 changed value to: " + stampedRef.getReference() + ", stamp: " + stampedRef.getStamp());
            stamp = stampedRef.getStamp();
            // Change B (stamp 1) -> A (stamp 2)
            stampedRef.compareAndSet("B", "A", stamp, stamp + 1);
            System.out.println("Thread-4 changed value back to: " + stampedRef.getReference() + ", stamp: " + stampedRef.getStamp());
        });

        thread3.start();
        try { Thread.sleep(50); } catch (InterruptedException e) {}
        thread4.start();

        thread3.join();
        thread4.join();
        System.out.println("Final value: " + stampedRef.getReference() + ", Final stamp: " + stampedRef.getStamp());
    }
}

/*
✅ Expected Output:

--- 1. Demonstrating the ABA Problem with AtomicReference ---
Initial value: A
Thread-1 read the value: A
Thread-2 changed value to: B
Thread-2 changed value back to: A
Thread-1 CAS from 'A' to 'C' was successful? true
🔴 FAILURE: Thread-1 was fooled by the ABA problem!
Final value: C


--- 2. Demonstrating the Solution with AtomicStampedReference ---
Initial value: A, Initial stamp: 0
Thread-3 read value: A, stamp: 0
Thread-4 changed value to: B, stamp: 1
Thread-4 changed value back to: A, stamp: 2
Thread-3 CAS from 'A' (stamp 0) to 'C' was successful? false
🟢 SUCCESS: Thread-3 correctly detected the intermediate change (the stamp mismatch)!
Final value: A, Final stamp: 2
*/