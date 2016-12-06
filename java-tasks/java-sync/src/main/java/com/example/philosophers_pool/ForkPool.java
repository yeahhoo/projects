package com.example.philosophers_pool;

/**
 * @author Aleksandr_Savchenko
 */
public class ForkPool {

    private Fork[] forks;
    private volatile int freeForksCounter;
    private Object noFreeForksMonitor = new Object();

    public ForkPool(int forksNumber) {
        forks = new Fork[forksNumber];
        for (int i = 0; i <= forks.length - 1; i++) {
            forks[i] = new Fork(i);
        }
        freeForksCounter = forks.length;

    }

    public boolean takeForks(Philosopher philosopher) throws Exception {
        synchronized (noFreeForksMonitor) {
            while (!areThereFreeForks()) {
                noFreeForksMonitor.wait();
            }
        }
        boolean leftTaken = false;
        for (Fork fork : forks) {
            if (!leftTaken && philosopher.tryTakeLeft(fork)) {
                leftTaken = true;
                synchronized (noFreeForksMonitor) {
                    freeForksCounter--;
                }
            } else if (philosopher.tryTakeRight(fork)) {
                synchronized (noFreeForksMonitor) {
                    freeForksCounter--;
                }
                return true;
            }
        }
        dropForks(philosopher);
        return false;
    }

    public void dropForks(Philosopher philosopher) throws Exception {
        synchronized (noFreeForksMonitor) {
            if (philosopher.dropLeftFork()) {
                freeForksCounter++;
            }
            if (philosopher.dropRightFork()) {
                freeForksCounter++;
            }
            if (areThereFreeForks()) {
                noFreeForksMonitor.notifyAll();
            }
        }
    }

    private boolean areThereFreeForks() {
        return freeForksCounter >= 2;
    }


}
