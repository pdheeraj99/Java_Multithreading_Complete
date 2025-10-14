# 🚀 Phase 4: Advanced `ReentrantLock` Features

[⬅️ Prev: 10-reentrantlock-intro.md](./10-reentrantlock-intro.md)

Manam `ReentrantLock` basics and the critical `try-finally` block gurinchi nerchukunnam. Ippudu, manam mundu chapter lo anukunna "I wish I could..." list lo unna problems ni okkokkati ga solve cheddam.

## 1. Solving "I wish I could try for a lock" with `tryLock()`

Remember our first frustration? `synchronized` tho manam lock kosam forever wait cheyyalsi vachedi. `ReentrantLock` solves this with the `tryLock()` method. Idi manaki rendu options istundi:

*   `lock.tryLock()`: "Lock kosam try cheyyi. Dorikithe `true` ivvu, lekapothe ventane give up chesi `false` ivvu. Nenu wait cheyanu."
*   `lock.tryLock(long time, TimeUnit unit)`: "Lock kosam ee specific time varaku try cheyyi. Ee lopu dorikithe `true` ivvu, lekapothe time aypoyaka give up chesi `false` ivvu."
*   `lock.tryLock()`: Tries to acquire the lock immediately. If successful, it returns `true`. If the lock is held by another thread, it returns `false` *immediately* without blocking.
*   `lock.tryLock(long time, TimeUnit unit)`: Tries to acquire the lock within a given timeout. If it gets the lock within the time, it returns `true`. If the time expires, it returns `false`.

**Snippet:**
```java
if (lock.tryLock()) {
    try {
        // Got the lock! Do critical work.
    } finally {
        lock.unlock();
    }
} else {
    // Couldn't get the lock. Do some other work instead of waiting.
    System.out.println("Could not acquire lock, doing alternative work.");
}
```

## 2. Solving "I wish I could interrupt a waiting thread" with `lockInterruptibly()`

Mana rendava frustration: `synchronized` kosam wait chestunna thread ni manam `interrupt()` cheyalemu. Adi `BLOCKED` state lo untundi, mana maata vinadu.

`lock.lockInterruptibly()` ee problem ni solve chestundi. Ee method tho lock kosam wait chestunna thread, interruption ki respond avuthundi. Vere thread `interrupt()` ni call cheste, ee method `InterruptedException` tho fail avuthundi. Appudu aa thread wait cheyadam aapi, vere pani chesukovachu.

This is crucial for building responsive and cancellable applications.

**Snippet:**
```java
try {
    // This will wait for the lock, but can be interrupted.
    lock.lockInterruptibly();
    try {
        // Got the lock. Do work.
    } finally {
        lock.unlock();
    }
} catch (InterruptedException e) {
    // The wait was cancelled by another thread calling interrupt().
    System.out.println("I was interrupted while waiting for the lock. I'll do something else.");
    // It's good practice to restore the interrupted status
    Thread.currentThread().interrupt();
}
```

## 3. Solving "I wish the lock was fair" with Fairness Policy

Mana third frustration: `synchronized` anedi unfair. Evaru mundu vacharu anedi daaniki anavasaram. A new thread might "barge in" and steal the lock from a thread that has been waiting patiently for a long time. Ee process lo, konni threads eppatiki chance rakunda starve avvochu.

`ReentrantLock` manaki oka choice istundi. By default, it's also unfair (for performance reasons). Kaani, manaki fairness kavali anukunte, we can create a **fair** lock. A fair lock respects a FIFO (First-In, First-Out) waiting queue. Evaraite mundu vachi wait chestunnaro, vallake next chance vastundi.

**How to create a fair lock:**
```java
// Pass 'true' to the constructor
private final ReentrantLock fairLock = new ReentrantLock(true);
```

**So, should we always use fair locks?** Not necessarily.
*   **Fairness comes at a cost.** There's extra bookkeeping involved to manage the queue of waiting threads, which significantly reduces the overall throughput (performance) of the application.
*   Use fair locks only when you have a genuine concern about thread starvation and the performance cost is acceptable. In most cases, the default unfair lock is the right choice.

[➡️ View Full Code Example: `ReentrantLockAdvanced.java`](./11-reentrantlock-features/ReentrantLockAdvanced.java)

---

## What's Next? 🤔

We've seen how flexible `ReentrantLock` is. But we still have a problem.

Imagine a data structure that is read very often but written to very rarely (e.g., a configuration object). If we use `ReentrantLock` (or `synchronized`), even read operations have to take the lock one-by-one. This is a huge bottleneck! It's perfectly safe for 100 threads to *read* the data at the same time, as long as no one is *writing*.

How can we create a lock that allows multiple concurrent readers but only a single exclusive writer?

This is the exact problem that `ReadWriteLock` was designed to solve. Let's explore it next!

[➡️ Next: 12-readwritelock-intro.md](./12-readwritelock-intro.md)