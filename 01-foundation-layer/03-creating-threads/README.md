# 3. Creating Threads: Let's Get Practical! 💻

Manam threads ante ento, vaati lifecycle ento chusam. Ippudu asalu vishayaniki vaddam: Java lo threads ni ela create cheyali? Manam chese panini (task) oka thread ki ela ivvali? Java lo deeniki chala approaches unnayi, pratidaniki oka specific use case untundi.

---
### Approach 1: Extending the `Thread` Class

Idi atyanta basic approach. Manam `java.lang.Thread` class ni extend chesi, daani `run()` method ni override chesi, mana task logic ni akkada rastam.

**Analogy:** Idi meeru oka "worker" laaga dress chesukovadam lantiది. Meeru `Thread` class ni extend cheyadam valla, mee class eh oka thread ga maripotundi.

```java
// MyWorkerThread.java
class MyWorkerThread extends Thread {
    // 1. run() method ni override cheyali. Ee method lo unna code eh
    //    kotha thread lo execute avtundi.
    @Override
    public void run() {
        System.out.println("Hello from MyWorkerThread! My name is: " + Thread.currentThread().getName());
    }
}

public class ExtendingThreadDemo {
    public static void main(String[] args) {
        // 2. Manam create chesina thread class ki object ni create cheyali.
        //    Ippudu thread NEW state lo untundi.
        MyWorkerThread thread = new MyWorkerThread();
        thread.setName("My-First-Thread"); // Debugging kosam thread ki peru pettadam manchi practice.

        // 3. thread.start() method ni call cheyali. Idi chala important.
        //    Idi JVM ki oka kotha OS thread ni create chesi, daaniki ee `run()`
        //    method ni assign cheyamani cheptundi. Thread RUNNABLE state loki veltundi.
        //    Direct ga thread.run() call cheyakudadhu! Ala cheste, adi normal method
        //    call laga main thread lo ne execute avtundi.
        thread.start();
    }
}
```

**When to use:** Chala simple cases lo, or meeru thread behavior ni (e.g., `interrupt()` lanti methods) override cheyali anukunnappudu matrame.

---
### Approach 2: Implementing the `Runnable` Interface (Preferred 👍)

Idi atyanta common and recommended approach. Ikkada manam mana task ni oka separate class lo `Runnable` interface ni implement chesi rastam.

**Analogy:** Idi oka "to-do list" (`Runnable`) ni create chesi, daanini oka "worker" (`Thread`) ki ivvadam lantiది. Worker veru, pani veru. Ee separation code ni clean ga unchutundi.

```java
// MyRunnableTask.java
// 1. Runnable interface ni implement cheyali. Idi oka functional interface.
class MyRunnableTask implements Runnable {
    // 2. run() method lo mana task logic ni rayali.
    @Override
    public void run() {
        System.out.println("Hello from MyRunnableTask! Executing in thread: " + Thread.currentThread().getName());
    }
}

public class ImplementingRunnableDemo {
    public static void main(String[] args) {
        // 3. Task object ni create cheyali. Idi pani, worker kaadu.
        MyRunnableTask task = new MyRunnableTask();

        // 4. Thread object ni create chesi, constructor lo mana task ni pass cheyali.
        Thread thread = new Thread(task);
        thread.setName("My-Runnable-Worker");

        // 5. Thread ni start cheyali.
        thread.start();
    }
}
```
**Why is this better?**
1.  **Separation of Concerns:** Task logic (pani) anedi thread mechanism nunchi separate ga untundi.
2.  **Flexibility:** Java lo multiple inheritance ledu. So, mi class already `extends MyBaseClass` chestu unte, adi `extends Thread` cheyaledu. Kani, adi `implements Runnable` cheyagaladu.
3.  **Reusability:** Oke `Runnable` task object ni, chala threads tho run cheyochu.

---
### Approach 3: `Callable` and `Future` (For Threads That Return Results 🎁)

`Runnable` yokka `run()` method emi return cheyadu (`void`). Mari, oka thread tana pani chesaka, oka result ni return cheyali ante? For example, oka network call chesi, vachina data ni return cheyali. Ikkade `Callable` and `Future` vastayi.

**Analogy:** Meeru pizza order chesaru (`Callable` task submit chesaru). Vadu meeku ventane oka receipt (`Future`) istadu. Pizza inka ready avvaledu. Meeru aa receipt pattukuni, pizza ready ayyaka (`future.get()`), daanini teeskuntaru.

```java
// MyCallableTask.java
// 1. Callable<V> ni implement cheyali. Ikkada V anedi manam return chese value yokka type.
class MyCallableTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        // 2. call() method lo mana logic rastam. Idi value ni return cheyochu.
        Thread.sleep(2000); // Simulate a long-running task
        return "This is the result from the long task!";
    }
}

public class CallableFutureDemo {
    public static void main(String[] args) throws Exception {
        // 3. Normal ga threads ni manage cheyadaniki, manam ExecutorService vadatham.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        MyCallableTask task = new MyCallableTask();

        // 4. Task ni submit cheste, adi manaki ventane oka Future object istundi.
        //    Idi aa result ki oka promise or placeholder.
        Future<String> future = executor.submit(task);

        System.out.println("Task submitted. Main thread is doing other work...");

        // 5. future.get() anedi blocking call. Ante, result vache varaku,
        //    main thread ikkada aagi, wait chestundi. Idi thread ni WAITING state loki pampistundi.
        String result = future.get();

        System.out.println("Result received: " + result);
        executor.shutdown();
    }
}
```

---
### Modern Approach: Lambda Expressions (Java 8+ ✨)

Java 8 vachaka, `Runnable` and `Callable` lanti functional interfaces kosam separate classes rayakkarledu. Direct ga lambda expressions vadavachu.

```java
public class LambdaThreadDemo {
    public static void main(String[] args) {
        // Runnable kosam lambda
        Runnable task = () -> System.out.println("Hello from a Lambda Runnable!");
        Thread thread = new Thread(task);
        thread.start();
    }
}
```
Idi code ni chala concise ga and readable ga chestundi.

---
### Thread Properties: Naming, Priority, and Daemon Status ⚙️

*   **Naming Threads:** Debugging lo threads ki peru pettadam chala important. `thread.setName("MyWorker");`
*   **Daemon Threads:** `thread.setDaemon(true);` call cheste, aa thread oka background thread ga marutundi. Anni non-daemon (user) threads complete ayipothe, daemon threads unna kuda JVM exit aypotundi.
*   **Priority:** Manam `thread.setPriority(int priority)` tho priority (1 to 10) set cheyochu, kani **idi reliable kaadu**. Thread scheduling anedi OS meeda depend avtundi, and different OSes priorities ni veru veru ga treat chestayi. As we learned, the OS scheduler makes the final decision on which thread runs on a **CPU Core**. **Never rely on thread priorities for program correctness.**

---

Ippudu manaki threads ni ela create cheyalo, vaati properties ento telisindi. With this, we have completed **Phase 1: Core Concepts**. Next, manam Java Memory Model (JMM) ane chala important and complex topic loki enter avtunnam. Manam hardware section lo nerchukunna reordering, visibility problems lanti concepts, ikkada Java lo ela kanipistayo chuddam. Ade **Phase 2: Memory Model Foundations**. Are you ready? 🔥