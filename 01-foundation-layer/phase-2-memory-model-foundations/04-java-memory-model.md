# 🚀 Phase 2: Java Memory Model (JMM) - The Rulebook for Threads

[⬅️ Prev: ../phase-1-core-concepts/03-creating-threads.md](../phase-1-core-concepts/03-creating-threads.md)

Manam Phase 1 lo threads ni ela create cheyalo nerchukunnam. Super! Kaani, asalu katha ippude modhalu avuthundi. Multiple threads okate data ni access chestunnappudu, chala unexpected things jaragachu. Why?

The answer lies in two main culprits:
1.  **Hardware Optimizations**: Modern CPUs chala fast ga undataniki, data ni main memory (RAM) nunchi direct ga teesukovu. Vaatiki sontanga chala fast unde **caches** untayi. Oka thread chesina change, inkoka thread ki ventane kanipinchakapovachu.
2.  **Compiler & JVM Optimizations**: Code ni fast ga run cheyadaniki, compiler or JVM mana code lo unna instructions ni reorder cheyochu.

Ee optimizations valla single-threaded code lo em problem raadu, kaani multi-threaded code lo chaos create avuthundi. Ee chaos ni control cheyadaniki, Java oka set of rules ni define chesindi. Aa rulebook ye **Java Memory Model (JMM)**.

> The JMM is a specification that guarantees the behavior of a properly synchronized Java program on any compliant hardware and JVM architecture.

Simple ga cheppalante, "Ee rules follow avvu, nee multithreaded program correct ga behave chestundi" ani JMM cheptundi.

## The Heart of JMM: The Happens-Before Relationship ❤️

JMM yokka core concept "Happens-Before". Idi chala powerful idea.

> If action A *happens-before* action B, then the results of A are guaranteed to be visible to B.

Ante, A lo jarigina memory writes anni, B ki kanipistayi ani guarantee. JMM konni specific situations lo ee happens-before relationship ni establish chestundi. Let's see the most important ones.

### 1. Program Order Rule
Oka single thread lo, code lo mundu vachina line, tarvata vachina line ki happens-before. Idi chala obvious anipinchina, reordering valla idi break avvochu, kaani JMM ee guarantee istundi.

### 2. Monitor Lock Rule (The `synchronized` Rule)
This is super important!
*   An **unlock** on a monitor lock *happens-before* every subsequent **lock** on that *same* monitor lock.

Ante, oka `synchronized` block nunchi oka thread vellipoye mundu (unlock), daani memory writes anni, ade lock ni acquire chesukuni `synchronized` block loki vachhe next thread ki (lock) visible avuthayi.

**Code Snippet Example:**

```java
// chinna code snippet to explain the rule
class SharedCounter {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) { // Action Y: Lock
            // Action X lo jarigina count=5 write ippudu visible avuthundi.
            count++;
        } // Action Z: Unlock
    }

    public void setup() {
        synchronized (lock) { // Action W: Lock
            count = 5;
        } // Action X: Unlock
    }
}
```
In the code above, if thread T1 calls `setup()`, its unlock (Action X) *happens-before* a subsequent lock by thread T2 calling `increment()` (Action Y). So, the `count = 5` write is guaranteed to be visible to T2.

### 3. Volatile Variable Rule
*   A **write** to a `volatile` variable *happens-before* every subsequent **read** of that *same* `volatile` variable.

`volatile` anedi `synchronized` kanna lightweight mechanism. Deeni gurinchi manam **Advanced Synchronization** chapter lo inka deep ga chuddam.

### 4. Thread Start Rule
*   A call to `Thread.start()` on a thread *happens-before* any action in the started thread.

Ante, main thread lo `t.start()` call cheyaka mundu unna variable writes anni, kottha thread ki visible avuthayi.

[➡️ View Full Code Example: `JMM_HappensBeforeExample.java`](./04-java-memory-model/JMM_HappensBeforeExample.java)

---

## What's Next? 🤔

JMM manaki rules ichindi, "ila `synchronized` vadithe, memory changes correct ga kanipistayi" ani cheppindi. Baagundi.

Kaani... asalu ee "memory changes kanipinchakapovadam" ante enti? Enduku kanipinchavu? CPU caches ee problem ni ela create chestayi? Ee "Visibility Problem" ni manam ಕళ్ళారా (with our own eyes) chusthe kaani, daani seriousness ardham kaadu.

Mana next chapter lo, manam sontanga oka program raasi, ee visibility problem ni live ga create cheddam. Appudu JMM and `synchronized` lanti constructs enduku antha mukhyamo meeku inka clear ga ardham avuthundi.

Ready to see some magic (and some problems)? Let's go! 👇

[➡️ Next: 05-visibility-problems.md](./05-visibility-problems.md)