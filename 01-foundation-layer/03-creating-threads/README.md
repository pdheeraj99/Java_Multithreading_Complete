# 3. Creating Threads: Giving Work to a New Thread 🧑‍💻

Manam `main` thread lo unnam. Mana program `main` method tho start ayyindi. Ippudu, manam konni panulanu parallel ga cheyali anukuntunnam. Ante, manam, `main` thread lo undi, kotha "worker" threads ni create chesi, vaatiki panulu ivvali. Mari, aa panulanu ela ivvali? Java lo deeniki chala approaches unnayi.

---
### Approach 1: Extending the `Thread` Class

Idi atyanta basic approach. Ikkada manam create chese class eh oka worker thread ga maripotundi.

**Analogy:** Idi meeru oka "worker" laaga dress chesukovadam lantiది.

```java
// MyWorkerThread.java
class MyWorkerThread extends Thread {
    @Override
    public void run() { // 2. Ee `run()` method lo unna code, kotha thread execute chestundi.
        System.out.println("Worker thread is running. My name is: " + Thread.currentThread().getName());
    }
}

public class ExtendingThreadDemo {
    public static void main(String[] args) {
        // 1. Manam, main thread lo, worker thread object ni create chestunnam.
        MyWorkerThread worker = new MyWorkerThread();
        worker.setName("My-First-Worker");

        // 3. Main thread, worker thread ni start chestundi.
        //    Ippudu rendu threads (main and worker) run avtunnayi.
        worker.start();
    }
}
```

---
### Approach 2: Implementing the `Runnable` Interface (Preferred 👍)

Idi better approach. Ikkada manam "pani" ni, "panivadu" nunchi separate chestam.

**Analogy:** Idi oka "to-do list" (`Runnable`) ni create chesi, daanini oka "worker" (`Thread`) ki ivvadam lantiది.

```java
// MyRunnableTask.java
class MyRunnableTask implements Runnable {
    @Override
    public void run() { // 2. Ee `run()` method, mana task logic ni define chestundi.
        System.out.println("Task is executing in thread: " + Thread.currentThread().getName());
    }
}

public class ImplementingRunnableDemo {
    public static void main(String[] args) {
        // 1. Main thread, "pani" (task) ni create chestundi.
        MyRunnableTask task = new MyRunnableTask();

        // 3. Main thread, oka kotha worker thread ni create chesi,
        //    daaniki ee task ni assign chestundi.
        Thread worker = new Thread(task, "My-Runnable-Worker");

        // 4. Main thread, worker thread ni start chestundi.
        worker.start();
    }
}
```
**Why is this better?** Task logic anedi thread mechanism nunchi separate ga untundi, idi clean code and provides more flexibility.

---
### Approach 3: `Callable` and `Future` (Getting a Result Back 🎁)

`Runnable` task, `main` thread ki emi return cheyadu. Mari, manam start chesina worker thread, tana pani ayyaka, `main` thread ki oka result ivvali ante?

**Analogy:** Meeru pizza order chesaru (`Callable` task). Vadu meeku ventane oka receipt (`Future`) istadu. Aa receipt tho, meeru tarvata mee pizza ni collect chesukovachu.

```java
// MyCallableTask.java
class MyCallableTask implements Callable<String> { // Returns a String
    @Override
    public String call() throws Exception {
        Thread.sleep(2000); // Worker thread is busy...
        return "Pizza is ready!"; // The result
    }
}

public class CallableFutureDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // NOTE: ExecutorService anedi Phase 7 lo detail ga chudaboye oka advanced concept.
        // Ippatiki, idi threads ni manage chese oka helper anukondi.

        // 1. Main thread, task ni create chesi, executor ki submit chestundi.
        Future<String> pizzaReceipt = executor.submit(new MyCallableTask());

        System.out.println("Main thread: Pizza order ichanu. Vere panulu chusukuntunna...");

        // 2. Main thread, receipt (`Future`) tho result kosam wait chestundi.
        //    `pizzaReceipt.get()` anedi blocking call. Result vache varaku `main` thread aagutundi.
        String result = pizzaReceipt.get();

        System.out.println("Main thread: " + result);
        executor.shutdown();
    }
}
```

---
### Thread Properties: How the `main` thread configures a worker

Manam `main` thread nunchi, create chese worker threads yokka konni properties ni set cheyochu.

*   **Naming Threads:** `worker.setName("My-Cool-Worker");` (Debugging ki chala helpful).
*   **Daemon Threads:** `worker.setDaemon(true);` `main` thread (user thread) aagipogane, ee background (daemon) threads kuda automatically aagipotayi.
*   **Priority:** `worker.setPriority(Thread.MAX_PRIORITY);` (Not reliable, OS will make the final decision).

---

Ippudu manaki `main` thread nunchi worker threads ni ela create cheyalo, vaati properties ento telisindi. With this, we have completed **Phase 1: Core Concepts**. Next, manam Java Memory Model (JMM) ane chala important and complex topic loki enter avtunnam. Ade **Phase 2: Memory Model Foundations**. Are you ready? 🔥