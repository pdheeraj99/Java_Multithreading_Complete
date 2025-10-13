import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 🚀 Thread Creation - Method 4: Using Callable and Future.
 *
 * `Runnable` `run()` method `void` kabatti, adi emi return cheyaledu.
 * What if a thread needs to compute and return a value? E.g., calculate the sum of a million numbers.
 *
 * Appude `Callable` and `Future` manaki help chestayi.
 *
 * `Callable<V>`:
 * - Idi `Runnable` laantide, kani `call()` ane method untundi.
 * - `call()` method can return a value of type `V`.
 * - It can also throw a checked exception.
 *
 * `Future<V>`:
 * - Represents the result of an asynchronous computation.
 * - Think of it as a "promise" or a "placeholder" for a result that will be available later.
 * - Methods like `isDone()`, `get()`, and `cancel()` ni provide chestundi.
 *
 * NOTE: `Callable` ni run cheyadaniki, manam `ExecutorService` ni vadali. Idi threads ni manage
 * chese oka powerful framework. Manam deeni gurinchi **Executor Framework** chapter lo
 * chala detail ga nerchukuntam. For now, let's see a simple example.
 */

// Step 1: Create a class that implements Callable<V>, where V is the return type.
class FactorialCalculator implements Callable<Long> {

    private final int number;

    public FactorialCalculator(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        this.number = number;
    }

    // Step 2: Implement the `call()` method. It will return a Long value.
    @Override
    public Long call() throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.println("🔢 " + threadName + " is starting to calculate factorial of " + number);
        long result = 1L;
        if (number == 0 || number == 1) {
            result = 1;
        } else {
            for (int i = 2; i <= number; i++) {
                result *= i;
                // Simulate a long calculation
                Thread.sleep(100);
            }
        }
        System.out.println("✅ " + threadName + " has finished the calculation.");
        return result;
    }
}

public class T04_UsingCallableAndFuture {

    public static void main(String[] args) {
        System.out.println("Main thread wants to get results from other threads.");

        // Step 3: Create an ExecutorService. Think of it as a manager of a thread pool.
        // `newSingleThreadExecutor` creates an executor that uses a single worker thread.
        ExecutorService executor = Executors.newSingleThreadExecutor();

        System.out.println("Submitting a factorial calculation task for number 10.");

        // Step 4: Create a Callable task instance.
        Callable<Long> task = new FactorialCalculator(10);

        // Step 5: Submit the task to the executor. It returns a Future object immediately.
        Future<Long> futureResult = executor.submit(task);

        System.out.println("Main thread submitted the task. The task is now running in the background.");
        System.out.println("Main thread can do other work while waiting...");

        try {
            // Let's do some other work
            Thread.sleep(500);
            System.out.println("Main thread is checking if the task is done...");
            if (futureResult.isDone()) {
                System.out.println("Task finished early!");
            } else {
                System.out.println("Task is still running. Main thread will wait for the result.");
            }

            // Step 6: Get the result from the Future object.
            // `futureResult.get()` is a blocking call. It will wait until the computation is complete.
            // Long result = futureResult.get();

            // We can also use a timeout to avoid waiting forever.
            Long result = futureResult.get(2, TimeUnit.SECONDS); // Wait for a maximum of 2 seconds.

            System.out.println("🎉 The factorial of 10 is: " + result);

        } catch (InterruptedException e) {
            // This exception is thrown if the current thread was interrupted while waiting.
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (ExecutionException e) {
            // This exception is thrown if the computation in the `call()` method threw an exception.
            System.err.println("The calculation threw an error!");
            e.printStackTrace();
        } catch (TimeoutException e) {
            // This is thrown if `get(timeout)` expires before the result is ready.
            System.err.println("The calculation took too long and timed out!");
            futureResult.cancel(true); // Attempt to cancel the running task.
        }

        // Step 7: Always shut down the executor service.
        // Otherwise, the JVM will not terminate.
        executor.shutdown();
        System.out.println("Executor service has been shut down.");
    }
}

/*
✅ Expected Output:

Main thread wants to get results from other threads.
Submitting a factorial calculation task for number 10.
Main thread submitted the task. The task is now running in the background.
Main thread can do other work while waiting...
🔢 pool-1-thread-1 is starting to calculate factorial of 10
Main thread is checking if the task is done...
Task is still running. Main thread will wait for the result.
✅ pool-1-thread-1 has finished the calculation.
🎉 The factorial of 10 is: 3628800
Executor service has been shut down.
*/