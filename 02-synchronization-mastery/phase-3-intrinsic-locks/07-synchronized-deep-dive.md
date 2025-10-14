# 🚀 Phase 3: The `synchronized` Keyword - Your First Line of Defense

[⬅️ Prev: ../../01-foundation-layer/phase-2-memory-model-foundations/06-java-memory-model-the-solution.md](../../01-foundation-layer/phase-2-memory-model-foundations/06-java-memory-model-the-solution.md)

Welcome to Synchronization Mastery! Manam Foundation Layer lo, `synchronized` lanti constructs lekunda code rasthe enni rakala problems (visibility, reordering) vastayo chusam. Ippudu, manam aa problems ni solve chese first and most important weapon gurinchi nerchukuntam: the `synchronized` keyword.

## The Core Idea: Intrinsic Locks (Monitors)

Java lo prathi object (`new Object()`, `new MyClass()`, etc.) ki venakala oka "hidden lock" untundi. Deenine **intrinsic lock** or **monitor lock** antaru. Ee lock ni at a time okate thread possess chesukogaladu.

Analogy: The Restroom Key 🔑
Imagine chesukondi, oka busy office lo okate okka single-person restroom undi. Ee restroom ni at a time okare use cheyagalaru. How is this enforced? With a key!

*   **The Restroom**: Idi mana shared resource (e.g., a critical section of code, a shared variable).
*   **The Key**: Idi mana intrinsic lock (monitor).
*   **A Person (Thread)**: Oka person restroom use cheyalante, mundu reception daggara key teesuvali. Key dorikithe, వాళ్ళు lopaliki velli lock chesukuntaru.
*   **Waiting People (Blocked Threads)**: Key vere valla daggara unte, inkoka person reception daggara wait cheyali. Vallu BLOCKED state lo untaru.
*   **Releasing the Key**: Pani aypoyaka, person bayataki vachi key ni reception lo istadu. Appudu, waiting lo unna next person aa key ni teesuuni lopaliki velthadu.

The `synchronized` keyword is Java's way of saying, "Ee code block (restroom) access cheyalante, mundu ee object yokka key (intrinsic lock) tecchukovali."

## How `synchronized` Solves Our Problems

`synchronized` block use cheste, JMM manaki rendu powerful guarantees istundi:
1.  **Mutual Exclusion**: At any given time, only one thread can execute a code block synchronized on the *same* object. Idi race conditions ni prevent chestundi.
2.  **Happens-Before Guarantee**: An unlock on a monitor *happens-before* a subsequent lock on the same monitor. Idi visibility and reordering problems ni solve chestundi. A thread exiting a synchronized block flushes all its variable changes to main memory, and the next thread entering the block is guaranteed to see them.

## Three Ways to Use `synchronized`

### 1. Synchronized Instance Method
Method signature lo `synchronized` keyword pedithe, aa method antha lock aypothundi. Ekkada lock edi? The instance of the object itself (`this`).

**Snippet:**
```java
class Counter {
    private int count = 0;

    // The lock is the 'Counter' object instance ('this').
    public synchronized void increment() {
        count++;
    }
}
```
If you have `counter1` and `counter2` objects, Thread-A locking `counter1.increment()` **does not** block Thread-B from calling `counter2.increment()`. The locks are on different objects.

### 2. Synchronized Block (The Most Flexible Way)
Sometimes you don't need to lock the entire method. Locking for a long time can hurt performance. `synchronized` block to the rescue! Idi manalni ഏ object meeda lock cheyalo, and entha code ni lock cheyalo anedi control cheyadaniki allow chestundi.

**Snippet:**
```java
class AnotherCounter {
    private int count = 0;
    private final Object lock = new Object(); // A dedicated lock object

    public void increment() {
        // Some non-critical code here...

        // Lock only the critical section
        synchronized (lock) {
            count++;
        }

        // More non-critical code here...
    }
}
```
**Best Practice**: Using a dedicated, private, `final` object for locking (`private final Object lock = new Object();`) is generally preferred over locking on `this`. It prevents other code from outside your class from grabbing your lock and causing unexpected behavior (livelocks, etc.).

### 3. Synchronized Static Method
What if you want to protect a `static` variable? A static variable belongs to the class, not an object instance. So, whose lock do we use? The `Class` object itself!

**Snippet:**
```java
class StaticCounter {
    private static int count = 0;

    // The lock is the 'StaticCounter.class' object.
    public static synchronized void increment() {
        count++;
    }
}
```
Ee lock application anthatiki okate untundi. A thread calling `StaticCounter.increment()` will block any other thread from calling it, regardless of the object instance.

[➡️ View Full Code Example: `SynchronizedTypesExample.java`](./07-synchronized-deep-dive/SynchronizedTypesExample.java)

---

## What's Next? 🤔

Super! Manam ippudu `synchronized` tho code ni ela protect cheyalo nerchukunnam. We can prevent race conditions and memory problems.

But what if a thread enters a synchronized block and realizes it can't proceed? For example, a "consumer" thread wants to take an item from a shared queue, but the queue is empty. It's holding a valuable lock, but it's stuck. It needs to release the lock temporarily and wait until a "producer" thread adds an item.

How do we make threads coordinate and communicate with each other like this? Just locking is not enough. We need a way for threads to pause and resume based on certain conditions.

This is where `wait()`, `notify()`, and `notifyAll()` come into play. Are you ready to learn how to make your threads talk to each other?

[➡️ Next: 08-wait-notify-intro.md](./08-wait-notify-intro.md)