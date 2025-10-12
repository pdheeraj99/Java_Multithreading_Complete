import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// `Runnable` tho problem enti ante, adi emi return cheyadu.
// Oka worker thread nunchi `main` thread ki result kavali ante, manam `Callable` vadali.
class MyCallableTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("Worker thread: Task start ayyindi, 2 seconds pani chestunna...");
        Thread.sleep(2000);
        System.out.println("Worker thread: Task poorthi ayyindi, result pampistunna.");
        return "Pizza is ready!";
    }
}

public class CallableFutureDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // NOTE: ExecutorService anedi oka powerful framework. Deeni gurinchi manam
        // Phase 7: Thread Pools lo chala detail ga nerchukundam.
        // Ippatiki, idi threads ni manage chese oka helper ani anukondi.
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // 1. Main thread, task ni create chesi, executor ki submit chestundi.
        System.out.println("Main thread: Pizza order istunnanu...");
        Future<String> pizzaReceipt = executor.submit(new MyCallableTask());

        System.out.println("Main thread: Pizza order ichanu. Receipt vachindi. Ippudu nenu vere panulu chusukuntunna...");
        // Ee time lo, worker thread background lo pani chestu untundi.

        // 2. Main thread, receipt (`Future`) tho result kosam wait chestundi.
        //    `pizzaReceipt.get()` anedi blocking call. Result vache varaku `main` thread aagutundi.
        System.out.println("Main thread: Naa panulu ayipoyayi, ippudu pizza kosam wait chestunna.");
        String result = pizzaReceipt.get();

        System.out.println("Main thread: Great! " + result);

        // 3. Main thread, executor ni shutdown chestundi.
        executor.shutdown();
    }
}

/*
Expected Output:
================
Main thread: Pizza order istunnanu...
Main thread: Pizza order ichanu. Receipt vachindi. Ippudu nenu vere panulu chusukuntunna...
Main thread: Naa panulu ayipoyayi, ippudu pizza kosam wait chestunna.
Worker thread: Task start ayyindi, 2 seconds pani chestunna...
Worker thread: Task poorthi ayyindi, result pampistunna.
Main thread: Great! Pizza is ready!
*/