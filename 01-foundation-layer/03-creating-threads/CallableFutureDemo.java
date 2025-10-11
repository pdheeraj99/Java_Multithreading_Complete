import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Approach 4: Using Callable and Future for results
// `Runnable` tho problem enti ante, adi emi return cheyadu.
// Oka thread nunchi result kavali ante, manam `Callable` vadali.
class MyCallableTask implements Callable<String> {

    // 1. `call()` method, `run()` laantidi, kani idi oka value ni return cheyagaladu
    //    and checked exceptions ni throw cheyagaladu.
    @Override
    public String call() throws Exception {
        System.out.println("Callable task started in thread: " + Thread.currentThread().getName());
        // Simulate a long-running task
        Thread.sleep(2000);
        return "Hello from Callable! This is the result.";
    }
}

public class CallableFutureDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 2. Callables ni run cheyadaniki, manam `ExecutorService` vadatham.
        //    Idi threads ni manage chese oka powerful framework. Manam deeni gurinchi
        //    tarvata chala detail ga nerchukundam.
        ExecutorService executor = Executors.newSingleThreadExecutor();

        MyCallableTask task = new MyCallableTask();

        System.out.println("Main thread is submitting the task...");
        // 3. `submit()` method, task ni thread pool ki istundi.
        //    Adi manaki ventane oka `Future` object ni istundi. Idi aa result ki
        //    oka promise or placeholder. Task inka background lo run avthundochu.
        Future<String> future = executor.submit(task);

        System.out.println("Task is submitted. Main thread can do other work while task runs in background...");
        // Ee time lo, main thread vere panulu chesukovachu.

        // 4. `future.get()` anedi blocking call. Ante, result vache varaku,
        //    main thread ikkada aagi, wait chestundi (`WAITING` state).
        System.out.println("Main thread is now waiting for the result...");
        String result = future.get();

        System.out.println("Result received: " + result);

        // 5. ExecutorService ni shutdown cheyadam chala important.
        executor.shutdown();
    }
}

/*
Expected Output:
================
Main thread is submitting the task...
Task is submitted. Main thread can do other work while task runs in background...
Main thread is now waiting for the result...
Callable task started in thread: pool-1-thread-1
Result received: Hello from Callable! This is the result.
*/