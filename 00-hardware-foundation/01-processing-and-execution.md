# Phase 0: The Hardware Foundation 🏛️

## 1. Processing & Execution

Java lo multithreading gurinchi nerchukune mundu, manam oka adugu venakki vesi, asalu ee code antha ekkada, ela run avtundo ardham chesukovali. Manam రాసే prati `if` condition, prati `for` loop, final ga computer yokka brain, a Central Processing Unit (CPU) lo ne execute avvali. Ee hardware foundation ardham kakapothe, Java Memory Model lanti complex topics eppatiki ardham kavu. So, let's start from the brain itself.

---

### CPU & Cores: The Restaurant Kitchen

Oka computer yokka **CPU (Central Processing Unit)** ni, manam oka busy restaurant kitchen lo unna Master Chef anukovachu. Atanu instructions (recipes) ni teeskuni, data (ingredients) ni process chesi, output (dishes) tayaru chestadu. Ee individual "cooking stations" eh manam **Cores** antam. Mi system lo 4 cores unte, at a time 4 different threads ni **nijanga parallel ga** run cheyochu.

#### Technical Deep Dive
*   **Von Neumann Architecture:** Modern CPUs follow this architecture. The core idea is that both program instructions and data are stored in the same memory. The CPU continuously performs a **Fetch-Decode-Execute cycle**.
    *   **Fetch:** Fetches the next instruction from memory.
    *   **Decode:** The **Control Unit (CU)** decodes the instruction to understand what operation to perform.
    *   **Execute:** The **Arithmetic Logic Unit (ALU)** performs the actual calculation (like addition, subtraction) or logical operation.
*   A **Core** is a complete implementation of this cycle. It has its own CU, ALU, and registers. A multi-core CPU is literally multiple independent processing units on a single chip.

---

### Hardware Threads & Hyper-Threading: The Chef's Hands

A **Hardware Thread** is the smallest physical unit of processing that the OS can schedule code on. Traditionally, oka core ki okate hardware thread undedi. **Hyper-Threading** (or SMT) anedi oka physical core ni, OS ki rendu logical cores la chupinchadam.

#### Technical Deep Dive
*   **Architectural State:** Oka thread ni run cheyadaniki, core ki konni registers avasaram. Ee registers (Program Counter, Stack Pointer, General-Purpose Registers) kalipi **Architectural State** antaru.
*   **Standard Core:** Has one set of execution resources (ALU, FPU) and one set of architectural state. It can only handle one hardware thread.
*   **Hyper-Threaded Core:** Has one set of execution resources, but **two sets of architectural state**. This allows the core to handle two hardware threads.
*   **How it Works:** Thread A memory kosam wait chestu (stalled) unte, daani state registers lo untundi. Core yokka execution units (ALU etc.) ippudu idle ga unnayi. Hyper-threading valla, aa idle execution units ni, core ventane Thread B (daani state, second set of registers lo undi) ki assign cheyagaladu. Ee context switch hardware level lo jaragadam valla, adi chala fast ga untundi. Manam tarvata "I/O-bound" tasks gurinchi matladinappudu, ee concept chala useful ga untundi.

---

### Clock Speed & Instruction Pipeline: The Chef's Speed and Assembly Line

**Clock speed** (GHz) anedi CPU oka second lo enni cycles ni execute cheyagalado చూపిస్తుంది. **Instruction Pipeline** anedi oka assembly line lantiది, idi pratoka cycle lo CPU ekkuva pani cheyadaniki help chestundi.

#### Technical Deep Dive
*   **Clock Cycle:** A CPU's operations are synchronized by a clock. A clock cycle is the smallest unit of time for the CPU. An instruction might take multiple cycles to complete.
*   **Classic 5-Stage Pipeline:** An instruction passes through several stages.
    1.  **IF (Instruction Fetch):** Instruction ni memory nunchi fetch cheyadam.
    2.  **ID (Instruction Decode):** Instruction ni decode chesi, registers nunchi operands ni read cheyadam.
    3.  **EX (Execute):** ALU ni use chesi calculation ni perform cheyadam.
    4.  **MEM (Memory Access):** Main memory ni read or write cheyadam.
    5.  **WB (Write Back):** Result ni register loki rayadam.
*   Pipelining valla, oka instruction EX stage lo unte, next instruction ID stage lo, and tarvata instruction IF stage lo undochu. This increases **throughput** (instructions per unit time).

---

### Context Switching & Branch Prediction: The Problems

Ee assembly line antha bagane undi, kani rendu pedda samasyalu unnayi: Context Switching and Branch Prediction.

#### Technical Deep Dive: Context Switching
*   **What is Switched?** When the OS decides to switch from Thread A to Thread B, it must save the **entire context** of Thread A. This includes:
    *   **Program Counter:** Next instruction to be executed.
    *   **General-Purpose Registers:** All the data the thread was actively working on.
    *   **Stack Pointer:** The thread's current position in its private stack.
    *   Other control registers and CPU flags.
*   This state is saved to a data structure in RAM called the **Process Control Block (PCB)** or Thread Control Block (TCB).
*   Then, the OS loads the context of Thread B from its TCB into the CPU registers. This entire save-and-load operation is pure overhead. Ee overhead valla, a thread can enter the `BLOCKED` or `WAITING` states, which we will see in the "Thread Lifecycle" module.

#### Technical Deep Dive: Branch Prediction
*   **The Problem:** A pipeline works best when the flow is linear. An `if` statement is a branch. The CPU doesn't know whether the `if` condition will be true or false until the EX stage. By then, it has already fetched the next few instructions.
*   **The Solution:** The CPU has a **Branch Predictor** unit that guesses the outcome of the branch. It uses a **Branch Target Buffer (BTB)** to store the history of recent branches.
*   **Misprediction Penalty:** If the prediction is wrong, all the instructions that were speculatively fetched and partially executed in the pipeline must be thrown away. This is called a **pipeline flush** or **pipeline bubble**. This can waste dozens of CPU cycles, causing a significant performance hit.

---

Ippudu manam CPU panulanu ela execute chestundo chala detail ga chusam. Kani, ee panulaku కావలసిన data ekkada untundi? Adi entha vegamga andistundo, antha vegamga CPU pani cheyagaladu. Next, manam computer yokka memory system, the Memory Hierarchy gurinchi nerchukundam.