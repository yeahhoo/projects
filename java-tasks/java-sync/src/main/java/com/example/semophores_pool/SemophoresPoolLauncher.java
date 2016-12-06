package com.example.semophores_pool;

import java.util.concurrent.TimeUnit;

/**
 * @author Aleksandr_Savchenko
 */
public class SemophoresPoolLauncher {

    public final static void main(String[] args) throws Exception {
        int poolCapacity = 3;
        int threadNumber = 5;
        long timePerJob = TimeUnit.MILLISECONDS.toMillis(20);
        MyCyclicBarrier barrier = new MyCyclicBarrier(threadNumber + 1 , () -> {
            System.out.println("BARRIER GOT THROUGH, HOOK ON ALL ACTIONS FINISHED THEIR ACTIVITIES. THIS PROGRAM HAS 2 BARRIER POINTS");
        });
        SemaphorePool pool = new SemaphorePool(poolCapacity);
        for (int i = 0; i < threadNumber; i++) {
            Thread thread = new Thread(new WorkerThread(i, pool, timePerJob, barrier));
            thread.start();
        }
        barrier.await();
        System.out.println("OVERCOME FIRST BARRIER");
        barrier.await();
        System.out.println("OVERCOME SECOND BARRIER");
        System.out.println("MAIN FINISHED");
    }

}
