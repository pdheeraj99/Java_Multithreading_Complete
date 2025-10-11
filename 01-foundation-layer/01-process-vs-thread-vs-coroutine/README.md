# Phase 1: Core Concepts - The Software Story

## 1. Process vs. Thread vs. Coroutine

Manam **Hardware Foundation** complete chesam! 🎉 Ippudu manaki CPU, Cores, Caches, and Memory gurinchi baga telusu. Aa knowledge tho, manam ippudu software abstractions loki enter avtunnam. Concurrent ga panulu cheyadaniki, manam Operating System (OS) and programming languages manaki icche tools ni vadali. Ee tools lo, manam mudu peru regular ga vintam: Process, Thread, and Coroutine. Ee muditiki madhya unna thedalu telusukovadam chala mukhyam.

---
### Process: The Heavyweight Champion 🥊

Manam mundu anukunnattu, a **process** is a program in execution. The key word is **isolation**.

**Analogy:** A Process is like a completely separate factory building 🏭. It has its own security, its own resources (power, water), and its own address. Nothing inside can see out, and nothing outside can see in without going through a formal security checkpoint.

#### Technical Deep Dive: Memory Layout 🧠
A process has its own private memory space, typically organized into:
*   **Code Segment:** The machine code of the program.
*   **Data Segment:** Static and global variables.
*   **Heap:** For dynamically allocated memory (e.g., `new Object()` in Java).
*   **Stack:** For local variables, function parameters, and return addresses. Each thread within the process gets its own stack.

This strong isolation makes processes very robust but also very heavyweight.

---
### Thread: The Lightweight Worker 🏃‍♂️

A **thread** is the smallest unit of execution *within* a process.

**Analogy:** Threads are like the workers *inside* the factory (Process). All workers share the factory's common resources (the Heap, Code, and Data segments), but each worker has their own personal toolkit and notepad (their private Stack).

#### Technical Deep Dive: Context Switching Costs 💰
As we learned in the hardware foundation, a **Context Switch** is the process of saving one task's state and loading another's.
*   **Process Context Switch:** When the OS switches from one process to another, it's a very expensive operation. It has to save the entire state of the first process (all its registers, memory maps, page tables, etc.) and then load the entire state of the second process. As we saw, the **TLB is flushed**, which adds to the cost. This is slow.
*   **Thread Context Switch:** When the OS switches between threads *of the same process*, it's much cheaper. Since they share the same memory space, the OS only needs to save and load the thread-specific state: the Program Counter, the Stack Pointer, and the general-purpose registers. The memory maps and TLB can often remain the same. This is why threads are considered "lightweight".

---
### Coroutine: The Cooperative Multitasker 🤝

Ippudu manam oka kotha, inka lightweight concept ni chuddam: **Coroutine**. Threads ni OS manage chestundi, kani coroutines ni programming language runtime or a library manage chestundi.

**Analogy:** Coroutines are like two chefs sharing a *single* cooking station (a single thread). Chef A cooks until he needs to wait for the water to boil. Instead of just standing there, he says to Chef B, "Okay, the station is yours for a few minutes while I wait." He voluntarily gives up control. When the water boils, Chef B gives control back to Chef A.

#### Technical Deep Dive 🔧
*   **Cooperative vs. Preemptive:** Threads are typically **preemptively multitasked**. The OS can interrupt a thread at any time to give another thread a chance to run. Coroutines are **cooperatively multitasked**. A coroutine runs until it explicitly and voluntarily **yields** control.
*   **User-space Scheduling:** The OS doesn't know about coroutines. They are scheduled entirely in "user-space" by the application or language runtime. This means a context switch between coroutines doesn't involve a slow system call to the OS kernel. It's just a simple function call, not a full OS-level context switch.
*   **Relation to Virtual Threads:** Java's new **Virtual Threads** (from Project Loom) are a form of stackful coroutine managed by the JVM. They are extremely lightweight and are designed to make **I/O-bound** tasks much more scalable.

---
### Green Threads vs. Native Threads: Who is the Boss? 👑

Ee thread management evaru chestunnaru anedanini batti, manam threads ni rendu rakalu ga chudochu.

*   **Native Threads:** These are threads managed directly by the underlying **Operating System**. The OS scheduler is responsible for assigning them to **CPU cores**. When you create a `new Thread()` in modern Java, you are creating a native thread. This gives you true parallelism on multi-core systems.
    *   **Pro:** Can use multiple CPU cores. 👍
    *   **Con:** Can be heavyweight. The number of native threads you can create is limited by the OS. 👎

*   **Green Threads:** These are threads managed entirely by the **language runtime** (like the JVM) in user-space. The OS sees only one native thread (the JVM process) and has no idea that there are many "green threads" running inside it.
    *   **Pro:** Very lightweight and fast to create and switch between. 👍
    *   **Con:** Cannot run on multiple CPU cores simultaneously. If one green thread makes a blocking I/O call, it blocks the entire OS thread, and all other green threads inside the JVM have to wait. 👎

**Java's Journey:**
*   Very early versions of Java (1.1) used **green threads**.
*   From Java 1.2 onwards, Java switched to using **native threads**, which is the model we use today for `java.lang.Thread`.
*   **Virtual Threads** are a modern return to the green thread philosophy, but they solve the blocking I/O problem by integrating smartly with the JVM's scheduler.

---

Ippudu manam process, thread, and coroutine ante ento, vaati madhya unna thedalu ento chusam. Next, manam oka Java thread yokka jeevitha chakram (lifecycle) gurinchi inka deep ga nerchukundam. Let's see what happens after a thread is born! 👶