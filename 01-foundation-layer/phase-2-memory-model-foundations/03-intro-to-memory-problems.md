# 🚀 Phase 2: Introduction - The Dangers of Shared Memory

[⬅️ Prev: ../phase-1-core-concepts/03-creating-threads.md](../phase-1-core-concepts/03-creating-threads.md)

Congratulations! 🥳 Manam Phase 1 lo threads ni ela create cheyalo nerchukunnam. We are now able to create multiple "chefs" and assign them tasks. Everything seems to work perfectly.

So, are we done? Can we now write any multithreaded program?

Not so fast. Ikkade asalu challenge start avuthundi.

## The Double-Edged Sword: Shared Memory 🗡️

Manam `Process vs Thread` chapter lo matladukunnattu, threads yokka greatest strength enti? **Shared Memory**. All threads within a process can access the same data in the heap. Idi communication ni chala fast and easy ga chestundi.

Kaani, ee greatest strength ye, multithreading lo greatest weakness kuda. Idi oka double-edged sword.

Imagine chesukondi, iddari chefs (threads) okate recipe book (shared data) ni at a time update cheyadaniki try chestunnaru.
*   Chef-1 oka page lo "add 1 cup of sugar" ani rastunnadu.
*   Ade time lo, Chef-2 ade page lo "add 2 cups of salt" ani rastunnadu.

The final result could be a disaster! Maybe the page gets torn, or one instruction overwrites the other, or you end up with a salty-sweet dish that no one wants to eat. 🤢

This is what we call a **Race Condition**. Multiple threads "race" to access and modify the same resource, and the final outcome depends on the unpredictable order of their execution.

## The Unseen Enemies: Caches and Compilers

Ee race conditions ni inka dangerous ga cheyadaniki, manaki rendu "unseen enemies" unnayi:

1.  **CPU Caches**: Prathi CPU core ki tana sonta fast local cache untundi. Oka thread chesina change, ventane main memory loki vellakunda, aa local cache lo ne undipovachu. Vere thread ki aa change kanipinchadu.
2.  **Compiler/JVM Optimizations**: Performance kosam, compiler mana code lo unna instructions ni reorder cheyochu. Ee reordering single-threaded code lo safe ye, kaani multithreaded code lo anukoni results ni ivvochu.

Ee problems valla, manam raase code correct ga kanipinchina, runtime lo chala weird ways lo fail avvochu. The worst part? These bugs are incredibly hard to reproduce and debug. They might happen once in a million runs.

## So, What's Next?

Ee phase lo, manam ee dangers ni face-to-face choodabotunnam.
1.  First, we will see the **Visibility Problem**, where changes made by one thread are not visible to another.
2.  Then, we will see the **Reordering Problem**, where the order of execution is not what we expect.

Ee problems ni ardham chesukunna tarvatane, manam vaatini solve chese tools (`synchronized`, `volatile`, etc.) yokka real power ni appreciate cheyagalam.

Let's take our first step into this dangerous, but fascinating, new territory. Are you ready to see things go wrong?

[➡️ Next: 04-visibility-problems.md](./04-visibility-problems.md)