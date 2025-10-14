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

### An Expert Question: Why `notifyAll()`? Why not just `notify()`?

This is a fantastic question that separates intermediate developers from experts. `notify()` seems more efficient, right? It only wakes up one thread. `notifyAll()` wakes up everyone, which seems wasteful.

Here's the danger. Imagine:
*   2 Producer threads (P1, P2) are waiting because the buffer is full.
*   2 Consumer threads (C1, C2) are waiting because the buffer is empty.

Now, a thread runs `consume()` and calls `notify()`. The JVM is free to wake up *any* waiting thread. What if it wakes up P2?
*   P2 wakes up, checks the condition `while (buffer.size() == capacity)`.
*   The buffer is still full! So P2 goes right back to sleep (`wait()`).
*   The original signal is now lost. C1 and C2, the threads that could have actually made progress, were never woken up.

If this happens repeatedly, it's possible for all the consumers to be stuck sleeping forever, even when there are items in the buffer. This is called a "lost wakeup".

`notifyAll()` solves this by waking up **everyone**. Yes, P1 and P2 will wake up, see the buffer is still full, and go back to sleep. But crucially, C1 and C2 will *also* wake up, see the buffer is no longer empty, and proceed to consume the data.

**The Golden Rule:** Always use `notifyAll()` unless you are 100% certain that every single thread is waiting for the exact same condition and any thread can make progress upon waking up. When in doubt, `notifyAll()` is the safer, more robust choice.

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