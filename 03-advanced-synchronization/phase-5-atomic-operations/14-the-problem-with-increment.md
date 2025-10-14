# 🚀 Phase 5: The Problem with `i++` (It's Not Atomic!)

[⬅️ Prev: ../../02-synchronization-mastery/phase-4-explicit-locks/13-condition-variables.md](../../02-synchronization-mastery/phase-4-explicit-locks/13-condition-variables.md)

Welcome to a new world of synchronization! Ippati varaku manam `synchronized` and `ReentrantLock` lanti locks tho thread safety ni sadhincham. But locks can be slow. They can cause contention. What if there was another way?

Ee phase lo, manam high-performance, lock-free techniques gurinchi nerchukuntam. But first, we need to understand a very fundamental problem that these techniques solve.

## The Deceptively Simple `i++`

Oka simple counter ni increment cheyalante, manam `i++` or `++i` ani rastam. It looks like a single, indivisible operation. Kaani, CPU level lo, adi oka single operation kaadu. It's a lie!

The `i++` operation is actually a sequence of three separate instructions:
1.  **Read**: Read the current value of `i` from memory into a CPU register. (`read i`)
2.  **Modify**: Increment the value in the CPU register. (`register_value + 1`)
3.  **Write**: Write the new value from the register back to memory. (`write i`)

Ee three steps madhyalo, a thread can be paused by the OS scheduler at any time. Ikkade asalu problem start avuthundi.

### The Race Condition in Action

Imagine `i` is currently `10`, and two threads (Thread-A and Thread-B) both want to execute `i++` at the same time.

1.  **Thread-A** reads the value of `i` (10) into its register.
2.  *Context Switch!* The OS pauses Thread-A and runs Thread-B.
3.  **Thread-B** also reads the value of `i` (still 10) into *its own* register.
4.  **Thread-B** increments its register's value to 11.
5.  **Thread-B** writes the value 11 back to `i`. Now `i` is 11.
6.  *Context Switch!* The OS pauses Thread-B and resumes Thread-A.
7.  **Thread-A** has no idea what just happened. It looks at its own register, which still holds the value 10.
8.  **Thread-A** increments its register's value to 11.
9.  **Thread-A** writes the value 11 back to `i`.

**The Final Result: `i` is 11.**

Oh my God! Rendu threads `i++` ni execute chesayi, kaani final value `12` ki badulu `11` vachindi. **An increment was lost!** This is a classic race condition.

Manam `synchronized` or a `ReentrantLock` use chesi ee read-modify-write sequence ni protect cheyochu, but that introduces locking overhead. Is there a way to make the increment operation itself atomic, without explicit locks?

[➡️ View Full Code Example: `RaceConditionOnIncrement.java`](./14-the-problem-with-increment/RaceConditionOnIncrement.java)

---

## What's Next? 🤔

Ee chinna `i++` ke intha problem unte, inka complex operations sangathi enti? How do modern systems handle this?

The answer lies in special hardware instructions that modern CPUs provide. One of the most important is called **Compare-And-Swap (CAS)**. This magical instruction allows us to build lock-free data structures.

In our next chapter, we will explore CAS and the powerful `Atomic` variable classes (`AtomicInteger`, `AtomicLong`, etc.) that Java provides, which use CAS to give us a thread-safe way to perform operations like incrementing, without ever using a lock.

Ready to go lock-free? Let's dive in!

[➡️ Next: 15-atomic-variables-and-cas.md](./15-atomic-variables-and-cas.md)