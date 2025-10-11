# Phase 0: The Hardware Foundation 🏛️

## 2. Memory Hierarchy

Ippudu manam CPU panulanu ela execute chestundo chala detail ga chusam. Kani, ee panulaku కావలసిన data ekkada untundi? Adi entha vegamga andistundo, antha vegamga CPU pani cheyagaladu. Endukante, oka fast CPU with slow memory anedi, oka fast chef with a lazy assistant lantiది - chala time waiting lone potundi. Ee problem ni solve cheyadanike, modern computers lo oka **Memory Hierarchy** (మెమరీ సోపానక్రమం) use chestaru. Idi speed, cost, and size madhya oka trade-off.

---
### Registers: The Chef's Hands

CPU oka calculation (`5+3`) chestunnappudu, aa `5`, `3` ane values ni ekkada pettukuntundi? Daaniki atyanta daggara, atyanta vegamaina memory lo pettukuntundi. Ade **Registers**.

**Analogy:** Registers anevi chef yokka chetulu. Atanu pani chestunnappudu, కావలసిన ingredients ni chetilo ne pattukuntadu.

#### Technical Deep Dive
*   **Location:** Directly on the CPU die.
*   **Speed:** Fastest memory possible. Access time is typically a single CPU clock cycle.
*   **Size:** Extremely small (e.g., a 64-bit CPU might have a few dozen general-purpose registers, totaling only a few kilobytes).
*   **Developer Control:** We don't directly control registers. The compiler (e.g., `javac`) and the JVM's Just-In-Time (JIT) compiler are responsible for generating machine code that allocates and uses registers efficiently.

---
### Cache: The Chef's Personal Desk & Drawers

Prati sari main memory (RAM) varaku velladam anedi chala slow. Daanikante better ga, CPU regular ga use chese data ni, tana daggara unna chinna, fast memory lo store chesukuntundi. Ade **Cache**.

**Analogy:** Cache anedi chef yokka personal workspace. Regular ga use chese knives, spices, and small ingredients anni desk meeda ne untayi.

#### Technical Deep Dive: A Point-wise Breakdown

When the CPU needs data, it checks the cache levels in order.

*   **L1 Cache (The Desk Surface)**
    *   **Location:** Private to a single CPU core. Each core has its own exclusive L1 cache.
    *   **Size:** Very small, typically 32 KB to 64 KB per core.
    *   **Speed:** Extremely fast, only slightly slower than registers. Access time is just a few clock cycles.
    *   **Split Cache:** Often split into two parts: **L1i** for instructions (the recipe steps) and **L1d** for data (the ingredients), to prevent them from competing for the same cache space.

*   **L2 Cache (The Desk Drawer)**
    *   **Location:** In modern CPUs, L2 is also typically private to a single CPU core.
    *   **Size:** Larger than L1, typically 256 KB to 4 MB per core.
    *   **Speed:** Slower than L1 but much faster than RAM. Access time is around 10-20 clock cycles.
    *   **Role:** Acts as a backup for the L1 cache. If an L1 miss occurs, the CPU checks L2 before going to the shared L3.

*   **L3 Cache (The Shared Kitchen Counter)**
    *   **Location:** **Shared** among all cores on a single CPU chip. This is a critical point.
    *   **Size:** Much larger than L1/L2, typically 8 MB to 64 MB for the entire chip.
    *   **Speed:** Slower than L1 and L2, but still significantly faster than RAM. Access time can be 40-100 clock cycles.
    *   **Role:** Its primary role is to act as a last line of defense before going to slow main memory. More importantly, it is the **main point of communication and data sharing between different cores**. When Core-A needs data that Core-B recently worked on, it's often found in the shared L3 cache, which is much faster than fetching it from RAM.

---
### RAM (Main Memory): The Kitchen's Pantry

Program run avthunnapudu, daani instructions and data antha ekkada untundi? Adi **RAM (Random Access Memory)** lo untundi.

**Analogy:** RAM anedi kitchen yokka main pantry. Desk meeda pattani pedda ingredients, and regular ga vadani items anni ikkada untayi.

#### Technical Deep Dive
*   **Volatility:** RAM is **volatile memory**; its contents are lost when the power is turned off.
*   **Size vs. Speed:** It is significantly larger (measured in Gigabytes) than cache (measured in Megabytes) but also significantly slower (access times are in the hundreds of clock cycles).
*   **OS Role:** When a program starts, the OS loader copies the executable code and data from the hard disk (Storage) into RAM. All threads of a process share the same view of RAM.

---
### Cache Line: Pantry nunchi Single Item Tecchukogalama?

CPU ki oka `int x` (4 bytes) kavali anukunte, cache system ਕੇవలం aa 4 bytes ni matrame RAM nunchi teeskodu. Enduku?

**Analogy:** Meeru pantry (RAM) ki velli, oka biscuit kosam vellaru anukundam. Meeru aa okka biscuit ni teeskoni raru kada? Meeru aa biscuit packet antha teeskuni vachi, desk meeda pettukuntaru.

#### Technical Deep Dive
*   Cache transfers data from RAM in fixed-size blocks called **Cache Lines**. The typical size of a cache line today is **64 bytes**.
*   This design leverages the **principle of spatial locality**: if a program accesses a memory location `addr`, it's highly likely to access nearby locations (`addr + 1`, `addr + 2`) soon.
*   Fetching an entire cache line improves performance because the cost of accessing RAM is high; it's more efficient to grab a whole chunk of data at once. As we will see in the next section, this design is the direct cause of the **False Sharing** performance problem in multithreading.

---
### Cache Coherence: Multiple Chefs, One Recipe Book

Multi-core systems lo, pratoka core ki sonthanga L1/L2 caches untayi. Appudu, oke data (`int x`) ki multiple copies undochu - okati RAM lo, and inkokati pratoka core yokka cache lo. Ee copies anni consistent ga undela chuse mechanism eh **Cache Coherence**.

**Analogy:** Iddaru chefs (Core 1, Core 2) oke recipe book (`x`) ni use chestunnaru. Iddariki aa book yokka photo copy (cache) undi. Chef 1 tana copy lo oka ingredient ni marchadu. Aa vishayam Chef 2 ki teliyali.

#### Technical Deep Dive
*   This is a hardware-level problem solved by **Cache Coherence Protocols**, the most common of which is **MESI (Modified, Exclusive, Shared, Invalid)**.
*   Each cache line is tagged with a MESI state. When a core writes to a cache line, the hardware sends signals over the memory bus to other cores. This can either **invalidate** their copies (telling them their copy is stale) or **update** their copies with the new data.
*   This process is the root cause of **visibility problems**. The time it takes for a write on one core to become visible to another is not instantaneous. We will see later how Java's `volatile` keyword directly influences this behavior by creating **Memory Barriers**.

---
### TLB (Translation Lookaside Buffer): The Pantry's Index Card

Mana Java program lo `new Object()` ani rasthe, adi oka **virtual address**. Kani RAM lo daaniki oka **physical address** untundi. Ee virtual-to-physical address translation ni OS, **page tables** use chesi chestundi.

**Analogy:** Pantry lo pratoka item ki oka specific location (e.g., Rack 3, Shelf 4) untundi. Ee locations anni oka pedda register book (Page Table) lo raasi untayi. Prati sari item kosam vellinappudu, ee pedda book chudatam chala time teeskuntundi. Anduke, chef tana daggara oka chinna index card (TLB) maintain chestadu, andulo regular ga velle items locations raasukuntadu.

#### Technical Deep Dive
*   The TLB is a small, hardware-based cache that stores recent virtual-to-physical address translations.
*   When the CPU generates a virtual address, it first checks the TLB. If it's a **TLB hit**, the physical address is retrieved instantly.
*   If it's a **TLB miss**, the CPU must perform a slow "page walk" by reading through the multi-level page tables in memory to find the physical address. This is a significant performance penalty.
*   The TLB is another reason why **Context Switches** (which we discussed in the previous section) are expensive: when switching between processes that don't share memory, the TLB must be flushed.

---

Ippudu manaki CPU and memory ela pani chestayo detail ga telusu. Next, manam ee knowledge ni use chesi, performance ni koliche metrics and common performance problems gurinchi nerchukundam.