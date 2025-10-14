# 🚀 Phase 5: Atomic Variables & Compare-And-Swap (CAS)

[⬅️ Prev: 14-the-problem-with-increment.md](./14-the-problem-with-increment.md)

Mana mundu chapter lo, manam `i++` lanti simple operation kuda concurrent environment lo entha disastrous o chusam. Thousands of increments were lost! Of course, manam `synchronized` or `ReentrantLock` tho aa `increment()` method ni protect cheyochu.

But... locking can be slow. When many threads compete for a lock, the OS has to suspend threads, manage queues, and perform context switches. This is expensive. What if there was a way to achieve thread safety *without* blocking?

## The Hardware to the Rescue: Compare-And-Swap (CAS)

Modern CPUs ee problem ni solve cheyadaniki manaki oka special, super-powerful instruction istayi. Ade **Compare-And-Swap (CAS)**.

CAS anedi oka atomic operation. Daani logic chala simple:
It takes 3 arguments:
1.  The memory location to update (`V`).
2.  The expected old value (`A`).
3.  The new value (`B`).

The CPU will atomically do this: "Ee memory location `V` lo unna value, nenu anukuntunna old value `A` tho samaanam ga unte ne, daanini kottha value `B` tho update cheyyi. Leka pothe, em cheyyaku. Success or failure anedi naku cheppu."

**Analogy: The Auction Bid 🏛️**
Imagine meeru oka auction lo unnaru.
*   The current bid for an item is **1000 Rs** (the value in memory, `V`).
*   You decide to bid **1100 Rs** (the new value, `B`).
*   You tell the auctioneer, "If the current bid is still **1000 Rs** (the expected old value, `A`), then accept my bid of **1100 Rs**."

What if, just as you were about to speak, someone else bid 1050 Rs? The current bid is no longer 1000 Rs. Your condition fails, and the auctioneer rejects your bid. You now have to check the new price (1050 Rs) and try again.

This is exactly how CAS works. It's an optimistic technique. It assumes everything is fine, but verifies before committing the change.

## Java's Atomic Variables

Java ee powerful CAS instruction ni `java.util.concurrent.atomic` package lo unna classes (`AtomicInteger`, `AtomicLong`, `AtomicBoolean`, `AtomicReference`, etc.) ద్వారా మనకి expose chestundi.

Ee classes `getAndIncrement()` (which is equivalent to `i++`), `getAndSet()`, and the fundamental `compareAndSet()` lanti methods ni provide chestayi.

When you call `atomicInteger.getAndIncrement()`, the JVM internally does something like this (it's a bit more complex, but this is the idea):
```java
// A conceptual loop inside getAndIncrement()
while (true) {
    int currentValue = get(); // Read current value
    int nextValue = currentValue + 1; // Calculate new value
    // Use CAS to try and set the new value
    if (compareAndSet(currentValue, nextValue)) {
        // Success! The value was updated atomically.
        return currentValue; // Return the old value
    }
    // Failure! Someone else changed the value. The loop will try again.
}
```
Because the `compareAndSet` part is a single, atomic hardware instruction, there is no possibility of a race condition. And because the thread just "spins" in a loop for a very short time if it fails, it avoids the heavy overhead of OS-level thread suspension and locking. This is called **optimistic locking**.

## The Solution to Our Counter Problem

Mana previous `RaceConditionOnIncrement.java` example lo, `UnsafeCounter` ki badulu, `AtomicInteger` ni use cheste, the problem is solved.

**The Fix:**
```java
// No locks, no synchronized, just a thread-safe atomic variable.
import java.util.concurrent.atomic.AtomicInteger;

class SafeCounter {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        // This is a thread-safe, atomic operation.
        count.getAndIncrement();
    }

    public int getCount() {
        return count.get();
    }
}
```
It's that simple!

[✅ View Full Code Example: `AtomicIncrementSolution.java`](./15-atomic-variables-and-cas/AtomicIncrementSolution.java)

---

## What's Next? 🤔

CAS is amazing! It seems like a perfect solution. But it has a very subtle, classic problem of its own.

Mana auction analogy ki veldam.
*   Current bid is 1000 Rs.
*   Someone bids 1200 Rs. The bid is now 1200.
*   That person cancels their bid. The bid goes back to 1000 Rs.
*   Now you come and say, "If the bid is 1000 Rs, I'll bid 1100 Rs."

Your CAS will succeed! But you missed the fact that the value changed from A -> B -> A in the middle. In many cases, this is fine. But in some situations (like managing resources in a lock-free stack), this can lead to serious bugs.

This is called the **ABA Problem**. How do we solve it? How do we track not just the value, but also the history of changes?

[➡️ Next: 16-the-aba-problem.md](./16-the-aba-problem.md)