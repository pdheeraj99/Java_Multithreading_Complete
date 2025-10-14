# 🚀 Phase 2: Visibility Problems - The "I Can't See You" Issue

[⬅️ Prev: 03-intro-to-memory-problems.md](./03-intro-to-memory-problems.md)

Mana previous introduction lo, multithreading lo unna dangers gurinchi matladukunnam. Ippudu aa dangers lo మొదటిదాన్ని, most common daanini face-to-face chuddam. Its name is the **Visibility Problem**.

## The Shocking Truth: Your CPU Cache Can Lie to You

Idhi konchem shocking ga anipinchachu, but it's the truth. Performance kosam, mana hardware manaki teliyakundane manalni mosam cheyochu. Let's see how.

Analogy: The Head Chef's Personal Notepad 👨‍🍳
Imagine mana Head Chef (Thread-1) unnadu. Atanu kitchen lo unna main recipe board (Main Memory/RAM) ni chusi, tana sonta personal notepad (CPU Cache) lo recipe copy chesukunnadu. Endukante, prathi sari aa board varaku velladam slow ani.

Ippudu, kitchen manager vachi, main board meeda recipe ni change chesadu ("add salt" ki badulu "add sugar" ani).

Kaani mana Head Chef ki ee vishayam teliyadu! Atanu tana personal notepad (cache) lone chuskuni, pani chestunnadu. Atanu inka పాత recipe ne follow avuthunnadu. The change made in the main memory was not *visible* to him.

Idhe, exactly idhe, mana computer lo jarugutundi.

Assistant Chef (Thread-2) ki ee kottha ingredient vachina sangathi teliyadu. Atanu tana personal notepad chuskuntadu, andulo "Special Masala" ledu. Atanu main board chusina, akkada inka update avvaledu. So, atanu aa ingredient ni vadaledu.

Idhe **Visibility Problem**. Thread-1 chesina change (writing to its own cache), Thread-2 ki kanipinchakapovadam.

### Why does this happen in CPUs?

Modern CPUs are incredibly fast. Main memory (RAM) is very slow in comparison. To bridge this speed gap, CPUs use multiple levels of small, super-fast memory called **caches**:
*   **L1 Cache**: Extremely fast, but very small. Each CPU core has its own L1 cache.
*   **L2 Cache**: Slower than L1, but larger. Often, each core has its own L2 cache.
*   **L3 Cache**: Slower than L2, but much larger. It's usually shared among all cores on a CPU.
*   **Main Memory (RAM)**: The slowest and largest memory, shared by all CPUs.

When a thread running on Core-1 modifies a variable, it might only update its L1 or L2 cache. That change needs to be "flushed" or "written-back" to main memory before a thread on Core-2 can see it. Ee process ventane jaragadu.

## Creating a Visibility Problem in Code

Ee problem ni demonstrate cheyadaniki oka classic example undi.
1.  Oka main thread untundi, adi `stop` ane oka boolean flag ni `false` ga set chestundi.
2.  Adi oka kottha thread (`worker`) ni start chestundi.
3.  The `worker` thread continuously loops as long as `stop` is `false`.
4.  The `main` thread waits for a second, and then sets `stop = true`.

Expectation: The `worker` thread should see `stop` become `true` and exit the loop.
Reality: The `worker` thread might loop forever! 😲

**Code Snippet Example:**

```java
// A simple class to demonstrate the problem
class LoopWorker {
    private boolean stop = false; // No 'volatile' or 'synchronized'

    public void startWork() {
        new Thread(() -> {
            System.out.println("Worker thread starting loop...");
            while (!stop) {
                // This is a "busy-wait" loop.
                // The value of 'stop' might be cached, so the change is never seen.
            }
            System.out.println("Worker thread stopped.");
        }).start();
    }

    public void stopWork() {
        System.out.println("Main thread setting stop = true");
        this.stop = true;
    }
}
```
In the code above, because `stop` is a plain variable, the JVM is free to perform an optimization. It might think, "This loop only reads `stop`, it never changes it inside the loop. I'll just read it once, see it's `false`, and then optimize the `while` check away into an infinite loop." This is a perfectly legal optimization for a single-threaded program, but it's disastrous here.

So, how do we fix this? How do we force the worker thread to always read the latest value of `stop` from main memory instead of its own cache?

### The Solution: `volatile`
The `volatile` keyword is the simplest solution for visibility problems. When you mark a variable as `volatile`:
1.  Any write to that variable is flushed directly to main memory.
2.  Any read of that variable comes directly from main memory, bypassing any local CPU cache.

This ensures that any thread reading the variable will always see the most recently written value.

**The Fix:**
```java
// Just add the 'volatile' keyword.
private volatile boolean stop = false;
```

[➡️ View Broken Code: `VisibilityProblem.java`](./05-visibility-problems/VisibilityProblem.java)
[✅ View Fixed Code: `VisibilitySolution.java`](./05-visibility-problems/VisibilitySolution.java)

---

## What's Next? 🤔

Chusara? Manam `synchronized` or `volatile` lanti JMM tools vadakapothe, code entha dangerously behave cheyochcho. The program might work 99 times and fail on the 100th time in production.

Visibility is just one side of the coin. There's another, even more subtle and dangerous problem lurking in the shadows: **Reordering**.

What if the CPU or compiler decides to reorder your instructions to be more efficient? What if you are initializing an object, and another thread sees the object reference *before* the object is fully constructed? It can lead to bizarre and hard-to-debug errors.

Are you ready to see how your carefully written lines of code can be shuffled around without your knowledge? Let's venture into the world of **Instruction Reordering**. 👇

[➡️ Next: 06-reordering-issues.md](./06-reordering-issues.md)