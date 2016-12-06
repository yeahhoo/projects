package com.example.philosophers_pool;

/**
 * @author Aleksandr_Savchenko
 */
public class PhilosophyPoolLaunch {

    public static void main(String[] args) {

        int number = 5;
        ForkPool forkPool = new ForkPool(2);

        Philosopher[] philosophers = new Philosopher[number];
        for (int i = 0; i <= philosophers.length - 1; i++) {
            philosophers[i] = new Philosopher(forkPool, i);
        }

        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }

    }
}
