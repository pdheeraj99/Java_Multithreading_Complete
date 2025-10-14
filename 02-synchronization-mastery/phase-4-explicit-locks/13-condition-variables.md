# 🚀 Phase 4: `Condition` Variables for Precision Waiting

[⬅️ Prev: 12-readwritelock-intro.md](./12-readwritelock-intro.md)

Manam `ReentrantLock` tho `synchronized` kanna better control sadhincham. `ReadWriteLock` tho read-heavy scenarios lo performance pencham. But there's one last piece of the puzzle.

## The Problem: The "One-Size-Fits-All" Waiting Room

Manam Phase 3 lo Producer-Consumer pattern ni `wait()` and `notifyAll()` tho implement chesam. Adi pani chesindi, kaani daanilo oka inefficiency undi.
*   Producers wait for the condition "buffer is not full".
*   Consumers wait for the condition "buffer is not empty".

Kaani, `synchronized(this)` use cheste, andaru (producers and consumers) okate object yokka "waiting room" lo wait chestaru.
When a producer adds an item and calls `notifyAll()`, it wakes up **everyone** – all waiting consumers AND all waiting producers.
Aa lechina producers malli buffer full ga undi ani chusi, ventane malli `wait()` loki vellipotharu. This is called a **spurious wakeup** from their perspective and it's inefficient. It's like a fire alarm that goes off for the whole building when only one apartment's toast is burning.

Wouldn't it be better if we had two separate waiting rooms?
1.  A "Not Full" room where only Producers wait.
2.  A "Not Empty" room where only Consumers wait.

That way, a Producer can wake up *only* the Consumers, and a Consumer can wake up *only* the Producers.

## The Solution: The `Condition` Interface

`ReentrantLock` allows us to create one or more `Condition` objects from it. Each `Condition` object provides its own set of `await()`, `signal()`, and `signalAll()` methods.
*   `condition.await()` is like `object.wait()`.
*   `condition.signal()` is like `object.notify()`.
*   `condition.signalAll()` is like `object.notifyAll()`.

This allows us to create those separate "waiting rooms" we wanted.

### The Golden Pattern for `Condition` Variables

We combine the `try-finally` pattern of `ReentrantLock` with the `while` loop pattern for waiting.

**Snippet (Producer waiting on the "notFull" condition):**
```java
private final ReentrantLock lock = new ReentrantLock();
private final Condition notFull = lock.newCondition(); // Condition for producers
private final Condition notEmpty = lock.newCondition(); // Condition for consumers

public void produce() throws InterruptedException {
    lock.lock();
    try {
        while (buffer.size() == capacity) {
            notFull.await(); // Wait in the "not full" room
        }
        // ... add item to buffer ...
        notEmpty.signalAll(); // Wake up everyone in the "not empty" room (the consumers)
    } finally {
        lock.unlock();
    }
}
```
Notice the precision: The producer adds an item and then calls `signalAll()` on the `notEmpty` condition, waking up only the consumers. It doesn't needlessly wake up other producers.

This is a much more efficient and scalable way to handle complex coordination scenarios compared to the single `wait/notify` mechanism of intrinsic locks.

[➡️ View Full Code Example: `ProducerConsumerWithConditions.java`](./13-condition-variables/ProducerConsumerWithConditions.java)

---

## What's Next? Congratulations on Mastering Explicit Locks! 🏆

You have now reached the pinnacle of explicit locking and coordination in Java. You've mastered:
*   `ReentrantLock` for flexible, interruptible, and fair locking.
*   `ReadWriteLock` for high-performance concurrent reading.
*   `Condition` variables for precise and efficient thread coordination.

You now have a complete, professional toolkit for handling almost any synchronization challenge.

In the next phase, we will shift gears. We will explore a different approach to concurrency that avoids locks altogether in many cases: **Atomic Operations** and **Concurrent Collections**. These are the high-performance tools that power some of the most scalable concurrent applications in the world.

Ready to learn how to achieve thread safety without always locking? Let's go!

[➡️ Next: Phase 5 - Atomic Operations](../../03-advanced-synchronization/phase-5-atomic-operations/14-atomic-variables.md)