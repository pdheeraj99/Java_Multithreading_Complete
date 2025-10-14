# 🚀 Phase 4: Advanced `ReentrantLock` Features

[⬅️ Prev: 10-reentrantlock-intro.md](./10-reentrantlock-intro.md)

Manam `ReentrantLock` basics and the critical `try-finally` block gurinchi nerchukunnam. But the real reason to use `ReentrantLock` over `synchronized` is for its advanced capabilities. Let's explore them.

## 1. `tryLock()` - The Non-Blocking Lock

`lock()` anedi blocking call. Ante, lock dorike varaku thread akkade block aypothundi. But what if you don't want to wait? What if you want to say, "If the lock is available, I'll take it. If not, I'll just do something else."

`tryLock()` is the answer.
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

## 2. `lockInterruptibly()` - The Cancellable Wait

Imagine a thread is waiting for a lock. With `synchronized`, it's stuck. There's no way to tell it, "Hey, stop waiting, I have a more important task for you."

`lock.lockInterruptibly()` solves this. A thread waiting in `lockInterruptibly()` can be, as the name suggests, interrupted. If another thread calls `interrupt()` on the waiting thread, `lockInterruptibly()` will throw an `InterruptedException`, and the thread can stop waiting for the lock and handle the interruption.

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

## 3. Fairness Policy - First-Come, First-Served

By default, `ReentrantLock` is **unfair**. When a lock is released, any thread can try to acquire it. A new, eager thread might "barge in" and grab the lock, even if other threads have been waiting for a long time. This can lead to **starvation**, where some threads never get a chance to run.

To prevent this, you can create a **fair** lock. A fair lock guarantees that the thread that has been waiting the longest will get the lock next (first-in, first-out or FIFO order).

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