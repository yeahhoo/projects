package com.example.philosophers_pool;

import java.util.concurrent.TimeUnit;

/**
 * @author Aleksandr_Savchenko
 */
public class Philosopher extends Thread {

    private Fork leftFork;
    private Fork rightFork;
    private int number;

    private static final long TIME_TO_THINK = TimeUnit.MILLISECONDS.toMillis(40l);
    private static final long TIME_TO_EAT = TimeUnit.MILLISECONDS.toMillis(20l);

    private ForkPool forkPool;

    public Philosopher(ForkPool forkPool, int number) {
        this.forkPool = forkPool;
        this.number = number;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (i < 5) {
                sleep(TIME_TO_THINK);
                if (forkPool.takeForks(this)) {
                    i = startEating(i);
                    System.out.println("Philosopher " + number + " ate times: " + i);
                    forkPool.dropForks(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean tryTakeLeft(Fork fork) throws Exception {
        if (leftFork == null && fork.tryTake()) {
            leftFork = fork;
            return true;
        }
        return false;
    }

    public boolean tryTakeRight(Fork fork) throws Exception {
        if (rightFork == null && fork.tryTake()) {
            rightFork = fork;
            return true;
        }
        return false;
    }

    public boolean dropFork(Fork fork) throws Exception {
        if (fork != null && fork.drop()) {
            if (fork == leftFork) {
                leftFork = null;
            } else {
                rightFork = null;
            }
            return true;
        }
        return false;
    }

    public boolean dropLeftFork() throws Exception {
        return dropFork(leftFork);
    }

    public boolean dropRightFork() throws Exception {
        return dropFork(rightFork);
    }

    private int startEating(int i) throws Exception {
        System.out.println("Philosopher " + number + " started eating: " + i);
        sleep(TIME_TO_EAT);
        return ++i;
    }

    @Override
    public String toString() {
        return "Philosopher " + number + " {" + "left " + leftFork + ", right " + rightFork + "} " + super.toString();
    }
}
