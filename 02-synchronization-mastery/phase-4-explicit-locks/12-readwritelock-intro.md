# 🚀 Phase 4: `ReadWriteLock` for High-Performance Reading

[⬅️ Prev: 11-reentrantlock-features.md](./11-reentrantlock-features.md)

Manam `ReentrantLock` tho fine-grained control sadhinchagalam ani chusam. Kaani, manam inka oka common performance problem ni solve cheyaledu.

## The Problem: Readers Get in Each Other's Way

Imagine a scenario:
*   You have a shared data structure, like a `Map` or a `List`.
*   Multiple threads need to **read** from this data structure.
*   Occasionally, one thread needs to **write** to it.

This "many readers, few writers" pattern is very common (e.g., application configuration, user session data, etc.).

If we use a `synchronized` block or a `ReentrantLock`, what happens?
Only one thread can access the data at a time, **regardless of whether it's reading or writing**.
If 100 threads want to read the data, they have to line up and do it one by one. This is a massive performance bottleneck! Reading is a harmless operation. It doesn't change the data. So, there is no reason why multiple threads shouldn't be allowed to read at the same time.

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