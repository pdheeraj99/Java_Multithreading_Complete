# 🚀 Phase 1: Creating Threads - Let's Bring Them to Life!

[⬅️ Prev: 02-thread-lifecycle-deep-dive.md](../02-thread-lifecycle-deep-dive.md)

Manam theory chala nerchukunnam. Process ante enti, Thread ante enti, Thread lifecycle lo emuntayo anni cover chesam. Ippudu aa "chefs" ni kitchen loki aahvaninche time vachindi! 👨‍🍳👩‍🍳

Java lo threads ni create cheyadaniki mukhyanga rendu popular ways unnayi. Kaani modern Java vachaka, inkonni convenient ways vachayi. Anni chuddam.

1.  **Extending the `Thread` Class**: Idi basic and straightforward way.
2.  **Implementing the `Runnable` Interface**: Idi flexible and most recommended way.
3.  **Using Anonymous Inner Classes**: `Runnable` ni implement cheyadaniki oka shortcut.
4.  **Using Lambda Expressions (Java 8+)**: Inka clean and concise way.
5.  **Using `Callable` and `Future`**: Thread nunchi oka result ni return cheyali anukunte.

Ee chapter lo, manam prathi approach ni detail ga chuddam, daani code examples tho saha.

## 1. Extending the `Thread` Class

Ee approach lo, manam `java.lang.Thread` class ni direct ga extend chesi, daani `run()` method ni override chestam.

**Advantages:**
*   Simple and easy for basic cases.
*   `this` keyword tho current thread object ni direct ga access cheyochu.

**Disadvantages:**
*   Java lo multiple inheritance ledu. So, meere class `Thread` ni extend chesthe, inka vere ഏ class ni extend cheyaleru. This is a major limitation.
*   Task (pani) ni, run chese mechanism nunchi separate cheyadam ledu. It violates the "separation of concerns" principle.

[➡️ View Code: `T01_UsingThreadClass.java`](./03-creating-threads/T01_UsingThreadClass.java)

## 2. Implementing the `Runnable` Interface

Idi industry lo ekkuvaga use chese and recommended approach. Ikkada manam `java.lang.Runnable` interface ni implement chestam. Ee interface lo `run()` ane okate method untundi.

**Advantages:**
*   **Flexibility**: Meere class inka vere classes ni extend chesukovachu.
*   **Good Design**: Task (`Runnable` object) ni, thread (`Thread` object) nunchi separate chestunnam. Okate `Runnable` instance ni multiple threads tho run cheyochu.
*   **Reusability**: `Runnable` tasks ni thread pools (manchi concept, tarvata nerchukuntam) tho easy ga use cheyochu.

[➡️ View Code: `T02_UsingRunnableInterface.java`](./03-creating-threads/T02_UsingRunnableInterface.java)

## 3. Using Lambda Expressions (Java 8+)

Java 8 vachaka, code chala simple aypoyindi. `Runnable` anedi oka "Functional Interface" (okate abstract method unna interface). So, daaniki manam lambda expression ni vadukovachu. Boilerplate code chala varaku taggipotundi.

[➡️ View Code: `T03_UsingLambda.java`](./03-creating-threads/T03_UsingLambda.java)

## 4. `Callable` and `Future` - Getting Results Back! 🎁

`Runnable` yokka `run()` method emi return cheyadu (`void`). Mari naaku thread execution ayyaka oka value kavali ante? For example, oka complex calculation chesi, result ni venakki ivvali.

Appude manaki `Callable` and `Future` picture loki vastayi.
*   **`Callable<V>`**: `Runnable` laantide, kaani `call()` ane method untundi and adi oka value (`V`) ni return chestundi. Exception ni kuda throw cheyagaladu.
*   **`Future<V>`**: Oka thread lo start chesina computation yokka result ni represent chestundi. Computation inka avvakapovachu. `Future` object tho manam result vachinda Leda check cheyochu, leda result vachhe varaku wait chesi, daanini `get()` chesukovachu.

Ee concepts ni manam **Executor Framework** chapter lo inka chala deep ga explore chestam. Ippudu oka basic example chuddam.

[➡️ View Code: `T04_UsingCallableAndFuture.java`](./03-creating-threads/T04_UsingCallableAndFuture.java)

---

## What's Next? 🤔

Wow! Manam ippudu Java lo threads ni ela create cheyalo, vaati nunchi results ela teeskovaalo kuda nerchukunnam. We are no longer just theorists; we are practitioners! 🛠️

So, we're done, right? We can write any concurrent program we want now?

Not quite. In fact, the most challenging and dangerous part of multithreading is just beginning. As we mentioned, threads share memory, which is their greatest strength... and their greatest weakness. When multiple threads touch the same data, things can go wrong in very strange ways.

Before we learn how to write complex concurrent applications, we MUST understand the dangers that lurk in the shadows. Are you ready to step into the dark side and see what happens when things go wrong?

[➡️ Next: Introduction to Memory Problems](../phase-2-memory-model-foundations/03-intro-to-memory-problems.md)