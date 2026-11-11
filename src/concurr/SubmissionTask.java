/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package concurr;

/**
 *
 * @author fernandoenad
 */
import java.util.concurrent.*;

public class SubmissionTask implements Runnable {
    private Learner learner;
    private Semaphore semaphore;
    private CyclicBarrier barrier;
    private CountDownLatch latch;

    private static int successful = 0;
    private static int failed = 0;

    public SubmissionTask(
            Learner learner,
            Semaphore semaphore,
            CyclicBarrier barrier,
            CountDownLatch latch) {

        this.learner = learner;
        this.semaphore = semaphore;
        this.barrier = barrier;
        this.latch = latch;
    }

    // Thread-safe counter methods
    public static synchronized void addSuccessful() {
        successful++;
    }

    public static synchronized void addFailed() {
        failed++;
    }

    public static synchronized int getSuccessful() {
        return successful;
    }

    public static synchronized int getFailed() {
        return failed;
    }

    @Override
    public void run() {

        boolean acquired = false;
        boolean submitted = false;

        try {
            System.out.println(learner.getName() + " (Group " + learner.getGroup() + ") is preparing.");

            Thread.sleep(500);

            // Wait for all five group members.
            barrier.await();

            // Only three learners may submit at once.
            semaphore.acquire();
            acquired = true;

            System.out.println(learner.getName() + " is submitting.");

            Thread.sleep(1000);

            System.out.println(learner.getName() + " submitted successfully.");

            submitted = true;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(learner.getName() + " was interrupted.");

        } catch (BrokenBarrierException e) {

            System.out.println(learner.getName() + " encountered a barrier error.");

        } finally {

            // Release the permit only if acquired.
            if (acquired) {
                semaphore.release();
            }

            // Record result.
            if (submitted) {
                addSuccessful();
            } else {
                addFailed();
            }

            // Mark the task completed even if it failed.
            latch.countDown();
        }
    }
}
