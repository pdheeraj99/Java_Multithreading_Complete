/**
 * 🚀 Thread Creation - Method 2: Implementing the Runnable Interface.
 *
 * Idi thread ni create cheyadaniki most recommended and flexible way.
 * Manam `java.lang.Runnable` interface ni implement chesi, daani `run()` method ni override chestam.
 *
 * Pros:
 * - Solves the single inheritance problem. Mana class inka vere useful class ni extend chesukovachu.
 * - Promotes "Separation of Concerns". The "task" (ChefTask) is separate from the "runner" (Thread).
 *   The same task can be run by different threads.
 * - Leads to more reusable and clean code design.
 *
 * Cons:
 * - Slightly more code: you have to create a Runnable instance and then a Thread instance.
 */

// Step 1: Create a class that implements the Runnable interface.
// Ee class lo manam cheyalsina "task" or "job" ni define chestam.
class ChefTask implements Runnable {

    private String taskName;

    public ChefTask(String taskName) {
        this.taskName = taskName;
    }

    // Step 2: Implement the `run()` method. Ee method lo thread cheyalsina logic untundi.
    @Override
    public void run() {
        System.out.println("💪 Task '" + taskName + "' is starting, assigned to thread: " + Thread.currentThread().getName());

        for (int i = 1; i <= 3; i++) {
            System.out.println("Thread '" + Thread.currentThread().getName() + "' is performing step " + i + " of task '" + taskName + "'");
            try {
                // Simulate some work
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Task '" + taskName + "' was interrupted.");
            }
        }
        System.out.println("✅ Task '" + taskName + "' is complete.");
    }
}

public class T02_UsingRunnableInterface {

    public static void main(String[] args) {
        System.out.println("Main thread is starting the process.");

        // Step 3: Create instances of our Runnable task.
        // Ivi just "tasks", inka threads kaadu.
        ChefTask cuttingVegetables = new ChefTask("Cutting Vegetables");
        ChefTask washingDishes = new ChefTask("Washing Dishes");
        ChefTask makingDough = new ChefTask("Making Dough");

        // Step 4: Create Thread objects and pass the Runnable tasks to their constructors.
        // Ikkada manam "task" ni "runner" (thread) ki assign chestunnam.
        Thread chef1 = new Thread(cuttingVegetables);
        chef1.setName("Chef-Ram");

        Thread chef2 = new Thread(washingDishes);
        chef2.setName("Chef-Sita");

        // We can even assign the same task to multiple threads!
        Thread assistantChef = new Thread(cuttingVegetables);
        assistantChef.setName("Assistant-Laxman");

        // Step 5: Call start() on the Thread objects.
        System.out.println("Assigning tasks to threads and starting them.");
        chef1.start();
        chef2.start();
        assistantChef.start();

        System.out.println("Main thread has delegated all tasks and will now exit.");
    }
}

/*
✅ Expected Output (Order will vary, but steps within a single task will be sequential):

Main thread is starting the process.
Assigning tasks to threads and starting them.
Main thread has delegated all tasks and will now exit.
💪 Task 'Cutting Vegetables' is starting, assigned to thread: Chef-Ram
💪 Task 'Washing Dishes' is starting, assigned to thread: Chef-Sita
💪 Task 'Cutting Vegetables' is starting, assigned to thread: Assistant-Laxman
Thread 'Chef-Ram' is performing step 1 of task 'Cutting Vegetables'
Thread 'Chef-Sita' is performing step 1 of task 'Washing Dishes'
Thread 'Assistant-Laxman' is performing step 1 of task 'Cutting Vegetables'
Thread 'Chef-Ram' is performing step 2 of task 'Cutting Vegetables'
Thread 'Assistant-Laxman' is performing step 2 of task 'Cutting Vegetables'
Thread 'Chef-Sita' is performing step 2 of task 'Washing Dishes'
Thread 'Chef-Ram' is performing step 3 of task 'Cutting Vegetables'
Thread 'Assistant-Laxman' is performing step 3 of task 'Cutting Vegetables'
Thread 'Chef-Sita' is performing step 3 of task 'Washing Dishes'
✅ Task 'Cutting Vegetables' is complete.
✅ Task 'Cutting Vegetables' is complete.
✅ Task 'Washing Dishes' is complete.
*/