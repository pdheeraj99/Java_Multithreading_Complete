# 🚀 Phase 3: The Producer-Consumer Pattern

[⬅️ Prev: 08-wait-notify-intro.md](./08-wait-notify-intro.md)

Manam `wait()` and `notify()` gurinchi theory nerchukunnam. Ippudu daanini practice lo pettadaniki time vachindi! The most famous and fundamental coordination problem that these methods solve is the **Producer-Consumer Problem**.

## The Problem Statement

Imagine we have:
1.  **A Producer**: Ee thread data ni or tasks ni generate chestundi.
2.  **A Consumer**: Ee thread aa data ni or tasks ni consume (process) chestundi.
3.  **A Shared Buffer**: Producer generate chesina data ni, Consumer teesuuni process chese mundu, oka temporary place lo store cheyali. Ee shared data structure (like a Queue or a List) ni buffer antam.

The rules are:
*   Producer buffer lo items add cheyali, kaani buffer **full** ga unte, adi wait cheyali. It shouldn't add to a full buffer.
*   Consumer buffer nunchi items teesukovali, kaani buffer **empty** ga unte, adi wait cheyali. It can't take from an empty buffer.

Ee coordination antha thread-safe ga jaragali, without any race conditions, deadlocks, or busy-waiting.

## The Broken Solution (without `wait`/`notify`)
First, let's see what happens if we try to solve this with just `synchronized` and loops. A naive developer might write code that "spins" or "busy-waits", constantly checking if the buffer state has changed. As we discussed, this is highly inefficient and can lead to deadlocks.

[➡️ View Broken Code: `ProducerConsumerProblem.java`](./09-producer-consumer-pattern/ProducerConsumerProblem.java)

This broken example will likely result in a deadlock or will consume 100% CPU, highlighting exactly why we need a better mechanism.

## The Correct Solution (with `wait()` and `notifyAll()`)

The elegant solution uses `wait()` and `notifyAll()` to pause and resume threads efficiently.

**Producer Logic:**
1.  Acquire the lock on the buffer.
2.  `while` the buffer is full:
    *   Call `buffer.wait()`. This releases the lock and puts the Producer to sleep.
3.  The buffer is not full, so add the item.
4.  Call `buffer.notifyAll()`. This is crucial! It wakes up any waiting Consumer threads, letting them know that an item is now available.
5.  Release the lock.

**Consumer Logic:**
1.  Acquire the lock on the buffer.
2.  `while` the buffer is empty:
    *   Call `buffer.wait()`. This releases the lock and puts the Consumer to sleep.
3.  The buffer is not empty, so remove and process the item.
4.  Call `buffer.notifyAll()`. This wakes up any waiting Producer threads, letting them know that space is now available in the buffer.
5.  Release the lock.

### Why `notifyAll()` instead of `notify()`?
In complex scenarios, you might have multiple producers and multiple consumers all waiting on the same lock. If a producer calls `notify()`, it might accidentally wake up another producer (who will see the buffer is still full and go back to sleep), instead of a consumer. This could lead to a situation where all consumers are sleeping and never get woken up.

`notifyAll()` is safer because it wakes up *all* waiting threads (producers and consumers). They will all re-check their condition in the `while` loop, and the ones who can proceed (the consumers, in this case) will do so. It's less performant than `notify()` but much less prone to deadlocks. As a rule of thumb, **always prefer `notifyAll()` until you are an expert and can mathematically prove that `notify()` is safe for your specific use case.**

[✅ View Fixed Code: `ProducerConsumerSolution.java`](./09-producer-consumer-pattern/ProducerConsumerSolution.java)

---

## What's Next? Congratulations on Mastering Intrinsic Locks! 🏆

You've done it! You have now mastered the most fundamental synchronization and coordination tools in the Java concurrency toolkit: `synchronized`, `wait()`, `notify()`, and `notifyAll()`. You can now protect shared data and make threads communicate effectively.

But... `synchronized` has some limitations.
*   You can't try to acquire a lock without blocking.
*   You can't interrupt a thread that is waiting for a lock.
*   You only get one condition per lock. What if you want separate waiting lines for "buffer is full" and "buffer is empty"?

To overcome these limitations, Java provides a more powerful and flexible set of locking tools in the `java.util.concurrent.locks` package.

Are you ready to level up your locking game? Let's dive into the world of **Explicit Locks** with `ReentrantLock`!

[➡️ Next: Phase 4 - Explicit Locks](../../02-synchronization-mastery/phase-4-explicit-locks/10-reentrant-lock.md)