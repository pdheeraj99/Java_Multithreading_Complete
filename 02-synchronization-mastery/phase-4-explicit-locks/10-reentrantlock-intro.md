# 🚀 Phase 4: `ReentrantLock` - The Advanced Locking Tool

[⬅️ Prev: ../phase-3-intrinsic-locks/09-producer-consumer-pattern.md](../phase-3-intrinsic-locks/09-producer-consumer-pattern.md)

Manam Phase 3 lo `synchronized`, `wait`, and `notify` gurinchi master chesam. We have a powerful hammer in our toolkit. But what happens when you need a screwdriver? Or a wrench?

`synchronized` is a great hammer: it's simple, effective, and gets the job done for many basic tasks. But as you build more complex applications, you'll start to feel its limitations. You'll find yourself saying, "I wish I could..."

## The "I Wish I Could..." Problem with `synchronized`

Here are the frustrations you will eventually face with `synchronized`:
*   "I wish I could **try** to get the lock, and if it's busy, just do something else instead of waiting forever." (`synchronized` always blocks).
*   "I wish I could **interrupt** a thread that's been waiting for a lock for too long." (`synchronized` waits are not interruptible).
*   "I wish I could make the lock **fair**, so the thread that's been waiting the longest gets it next." (`synchronized` makes no fairness guarantees).
*   "I wish I could have **separate waiting rooms** for my producers and consumers instead of waking everyone up with `notifyAll()`." (`synchronized` only has one wait set per object).

Ee frustrations ni solve cheyadaniki, Java developers manaki oka brand new, professional-grade toolkit icharu: the `java.util.concurrent.locks` package. Andulo unna mana first and most important tool ye `ReentrantLock`.

Here are some limitations of `synchronized`:
1.  **Can't Interrupt**: Oka thread `synchronized` block kosam wait chestunte, daanini manam interrupt cheyalem. Adi lock dorike varaku akkade `BLOCKED` state lo untundi, forever if needed.
2.  **No Timeout**: "Ee lock kosam 5 seconds wait cheyyi, appatiki rakapothe, give up and do something else" ane logic ni manam `synchronized` tho implement cheyalem.
3.  **Unfair by Default**: Lock release ayyaka, waiting lo unna threads lo దేనికి lock vastundo ane guarantee ledu. Eager threads might "barge" in, causing threads that have been waiting longer to starve.
4.  **Single Wait Set**: `wait/notify` use cheste, andaru (producers, consumers) okate waiting room lo untaru. Idi `notifyAll()` tho inefficient ga undochu.

Ee limitations ni overcome cheyadaniki, Java developers `java.util.concurrent.locks` package lo `Lock` interface ni and daani implementation `ReentrantLock` ni create chesaru.

## The Solution: `ReentrantLock`

`ReentrantLock` anedi `synchronized` kanna chala more flexible and powerful alternative. Idi `Lock` interface ni implement chestundi.

Think of it as a manual transmission car. It gives you more control, but it also means you have more responsibility.

### The Most Important Rule: The `try-finally` Block

`synchronized` block use cheste, lock automatically release avuthundi, even if an exception occurs. Kaani `ReentrantLock` tho, **you are responsible for releasing the lock**.

If you don't release the lock (e.g., because an exception was thrown), it will be held forever, and your application will deadlock. Anduke, the only correct way to use `ReentrantLock` is with a `try-finally` block.

**THE GOLDEN PATTERN (memorize this!):**
```java
// 1. Create a lock instance
private final ReentrantLock lock = new ReentrantLock();

public void doSomething() {
    // 2. Acquire the lock
    lock.lock();
    try {
        // 3. This is the critical section. Your protected code goes here.
        // ...
    } finally {
        // 4. Release the lock in the 'finally' block.
        // This GUARANTEES the lock is released, even if an exception happens.
        lock.unlock();
    }
}
```
**WARNING**: `unlock()` ni `finally` block lo pettakapovadam anedi oka serious bug. Never forget this pattern!

### What does "Reentrant" mean?
"Reentrant" ante "re-enterable". `synchronized` kuda reentrant ye.
It means if a thread already holds a lock, it can acquire the same lock again without blocking itself. The lock maintains a "hold count". Every time the thread locks, the count increments. Every time it unlocks, the count decrements. The lock is only fully released when the hold count becomes zero.

[➡️ View Full Code Example: `ReentrantLockBasics.java`](./10-reentrantlock-intro/ReentrantLockBasics.java)

---

## What's Next? 🤔

Okay, we've learned the basic `lock()` and `unlock()` pattern. But this doesn't show us why `ReentrantLock` is better than `synchronized`. The real power lies in its advanced features.

What if we could *try* to get a lock and give up if it's not available? What if we could make our locks "fair"? What if a waiting thread could be interrupted?

In our next chapter, we'll explore these powerful, advanced features that make `ReentrantLock` a true expert's tool.

[➡️ Next: 11-reentrantlock-features.md](./11-reentrantlock-features.md)