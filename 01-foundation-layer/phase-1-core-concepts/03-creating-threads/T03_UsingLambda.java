/**
 * 🚀 Thread Creation - Method 3: Using Lambda Expressions (Java 8+).
 *
 * Java 8 vachaka, manam `Runnable` lanti "Functional Interfaces" ni implement cheyadaniki
 * chala concise syntax vachindi, ade Lambda Expressions.
 *
 * A "Functional Interface" is any interface that has exactly one abstract method.
 * `Runnable` has only one method, `run()`, so it's a perfect candidate.
 *
 * Ee approach tho, manam separate class ni (like ChefTask) create cheyalsina avasaram ledu.
 * Boilerplate code chala varaku taggipotundi.
 */
public class T03_UsingLambda {

    public static void main(String[] args) {
        System.out.println("Main thread is using modern ways to hire chefs!");

        // --- Method 1: Using a separate Runnable with Lambda ---
        // Manam direct ga `Runnable` object ni create chesi, daaniki lambda expression ivvochu.
        // () -> { ... body ... } is the lambda expression.
        // () - represents the arguments to the run() method (em levu kabatti empty).
        // -> - separates the arguments from the body.
        // { ... } - is the body of the run() method.
        Runnable grillingTask = () -> {
            System.out.println("🔥 Grilling task started by thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000); // Grilling takes time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("✅ Grilling task finished.");
        };

        Thread chef1 = new Thread(grillingTask, "Chef-GrillMaster");
        chef1.start();


        // --- Method 2: Passing the Lambda directly to the Thread constructor ---
        // Manam inka concise ga, lambda ni direct ga `Thread` constructor ke pass cheyochu.
        // Idi chala common ga use chese pattern.
        Thread chef2 = new Thread(() -> {
            System.out.println("🍹 Blending juice task started by thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("✅ Blending juice task finished.");
        }, "Chef-JuiceExpert");

        chef2.start();


        // --- Method 3: Lambda with a loop ---
        Thread chef3 = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("📦 Packing food task started by thread: " + threadName);
            for (int i = 1; i <= 3; i++) {
                System.out.println(threadName + " is packing box #" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("✅ Packing food task finished.");
        }, "Chef-Packer");

        chef3.start();

        System.out.println("Main thread has started all modern chefs and is now finishing its work.");
    }
}

/*
✅ Expected Output (Order will vary):

Main thread is using modern ways to hire chefs!
Main thread has started all modern chefs and is now finishing its work.
🔥 Grilling task started by thread: Chef-GrillMaster
🍹 Blending juice task started by thread: Chef-JuiceExpert
📦 Packing food task started by thread: Chef-Packer
Chef-Packer is packing box #1
Chef-Packer is packing box #2
🍹 Blending juice task finished.
Chef-Packer is packing box #3
✅ Packing food task finished.
🔥 Grilling task finished.
*/