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

public class Main {
    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(3);
        CountDownLatch latch = new CountDownLatch(15);

        CyclicBarrier group1 = new CyclicBarrier(5);
        CyclicBarrier group2 = new CyclicBarrier(5);
        CyclicBarrier group3 = new CyclicBarrier(5);

        Learner[] learners = new Learner[15];

        // Create 15 learner objects.
        for (int i = 0; i < learners.length; i++) {

            int id = i + 1;
            int group = (i / 5) + 1;

            learners[i] = new Learner(
                id, "Learner " + id, group);
        }

        System.out.println(
            "=== EXAMINATION SUBMISSION SYSTEM ===");

        // Create and start threads.
        for (Learner learner : learners) {

            CyclicBarrier barrier;

            if (learner.getGroup() == 1) {
                barrier = group1;

            } else if (learner.getGroup() == 2) {
                barrier = group2;

            } else {
                barrier = group3;
            }

            SubmissionTask task = new SubmissionTask(
                learner,
                semaphore,
                barrier,
                latch
            );

            Thread thread = new Thread(task);
            thread.start();
        }

        try {
            // Wait for all learner tasks.
            latch.await();

            System.out.println(
                "\n=== FINAL SUBMISSION REPORT ===");

            System.out.println(
                "Total Learners: " + learners.length);

            System.out.println(
                "Successful Submissions: "
                + SubmissionTask.getSuccessful());

            System.out.println(
                "Failed Submissions: "
                + SubmissionTask.getFailed());

            System.out.println(
                "All 15 learner tasks completed!");

            System.out.println(
                "Final report generated.");

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "Report generation interrupted.");
        }
    }
}
