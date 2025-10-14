# 🚀 Phase 2: Introduction - The Dangers of Shared Memory

[⬅️ Prev: ../phase-1-core-concepts/03-creating-threads.md](../phase-1-core-concepts/03-creating-threads.md)

Congratulations! 🥳 Manam Phase 1 lo threads ni ela create cheyalo nerchukuni, oka pedda milestone ni complete chesam. Mana code kuda perfect ga pani chesindi. Everything seems fine.

Kaani, nenu mundu chapter lo cheppinattu, this is the most dangerous moment. Ikkade asalu katha, asalu challenge start avuthundi.

## The Double-Edged Sword: Shared Memory 🗡️

Manam `Process vs Thread` chapter lo nerchukunnam: threads yokka greatest strength enti? **Shared Memory**. Anni threads okate memory ni (mana restaurant kitchen lo unna ingredients ni) share chesukuntayi. Deeni valla communication chala fast ga, easy ga untundi. Idi manam pondhina varam (a boon).

But, ee varame, oka shaapam (a curse) laaga kuda maripothundi. Ee greatest strength ye, multithreading lo greatest weakness kuda.

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