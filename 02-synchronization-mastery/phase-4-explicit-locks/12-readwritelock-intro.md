# 🚀 Phase 4: `ReadWriteLock` for High-Performance Reading

[⬅️ Prev: 11-reentrantlock-features.md](./11-reentrantlock-features.md)

Manam `ReentrantLock` tho enni problems solve cheyacho chusam. It's a fantastic tool. But what if our main problem isn't fairness or interruptibility, but pure, raw **performance**?

## The Problem: The Readers' Traffic Jam

Imagine a very popular blog.
*   1000s of users (**Reader Threads**) are trying to read the articles at the same time.
*   Once in a while, the author (**Writer Thread**) logs in to fix a typo.

If we protect the blog's content with a standard `ReentrantLock` (or `synchronized`), what happens? A traffic jam!
Only one reader can access the content at a time. Reader-2 has to wait for Reader-1 to finish. Reader-3 has to wait for Reader-2. It's like a single-file line into a massive library.

This is incredibly inefficient. Reading data is a harmless operation; it doesn't change anything. Logically, all 1000 readers should be able to read the article simultaneously without any issue. The only time we need to block everyone is when the author is actively writing.

How can we implement a lock that is:
*   **Shared** for readers (allowing many concurrent readers).
*   **Exclusive** for writers (allowing only one writer, and blocking all readers).

## The Solution: `ReentrantReadWriteLock`

Java provides the perfect tool for this: `ReentrantReadWriteLock`.

Ee object manaki rendu separate locks istundi, derived from a single `ReadWriteLock` instance.
1.  A **Read Lock** (`lock.readLock()`)
2.  A **Write Lock** (`lock.writeLock()`)

These locks work together with a special set of rules:
*   If **no thread** holds the write lock, then **any number of threads** can acquire the read lock and read concurrently.
*   If **any thread** holds the write lock, then **no other thread** (neither reader nor writer) can acquire either lock until the write lock is released.
*   If **any thread** holds a read lock, a thread trying to acquire the write lock will be blocked until all read locks are released.

This provides the best of both worlds: high-performance, concurrent reading, and safe, exclusive writing.

### The Golden Pattern for `ReadWriteLock`

Just like `ReentrantLock`, you are responsible for releasing the locks, so the `try-finally` block is mandatory.

**Snippet for a Reader:**
```java
private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
private final Lock readLock = rwLock.readLock();

public String readData() {
    readLock.lock();
    try {
        // It's safe for many threads to be in here at once.
        // ... read the shared data ...
    } finally {
        readLock.unlock();
    }
}
```

**Snippet for a Writer:**
```java
private final Lock writeLock = rwLock.writeLock();

public void writeData(String data) {
    writeLock.lock();
    try {
        // Only one thread can be in here at a time.
        // All readers are blocked while this lock is held.
        // ... write to the shared data ...
    } finally {
        writeLock.unlock();
    }
}
```

[➡️ View Full Code Example: `ReadWriteLockExample.java`](./12-readwritelock-intro/ReadWriteLockExample.java)

---

## What's Next? 🤔

`ReadWriteLock` is a fantastic tool for performance optimization. But we still have the same issue we saw with `synchronized`: what if we need more sophisticated coordination than just locking?

With `synchronized`, we used `wait()` and `notify()` to make threads wait for specific conditions. Can we do something similar with `ReentrantLock`? What if we need multiple, separate "waiting rooms" for different conditions, all associated with a single lock?

This is where the `Condition` interface comes in. It's like `wait/notify` on steroids. Ready to unlock the final level of explicit lock coordination?

[➡️ Next: 13-condition-variables.md](./13-condition-variables.md)