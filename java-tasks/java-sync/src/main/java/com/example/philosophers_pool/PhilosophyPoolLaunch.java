package com.example.philosophers_pool;

/**
 * @author Aleksandr_Savchenko
 */
public class PhilosophyPoolLaunch {

    public static void main(String[] args) {

        int philosophersNumber = 5;
        int forksInPoolNumber = 2;
        ForkPool forkPool = new ForkPool(forksInPoolNumber);

        Philosopher[] philosophers = new Philosopher[philosophersNumber];
        for (int i = 0; i <= philosophers.length - 1; i++) {
            philosophers[i] = new Philosopher(forkPool, i);
        }

        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }
    }

}
