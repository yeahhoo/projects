package com.example.philosophers_pool;

import java.util.concurrent.TimeUnit;

/**
 * @author Aleksandr_Savchenko
 */
public class Philosopher extends Thread {

    private Fork leftFork;
    private Fork rightFork;
    private int number;
    private long timeInterval;

    private static final long TIME_TO_THINK = TimeUnit.SECONDS.toMillis(2l);
    private static final long TIME_TO_EAT = TimeUnit.SECONDS.toMillis(1l);

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
        if (leftFork == null && fork.tryTake(this)) {
            leftFork = fork;
            return true;
        }
        return false;
    }

    public boolean tryTakeRight(Fork fork) throws Exception {
        if (rightFork == null && fork.tryTake(this)) {
            rightFork = fork;
            return true;
        }
        return false;
    }

    public boolean dropLeftFork() throws Exception {
        if (leftFork != null) {
            if (leftFork.drop(this)) {
                leftFork = null;
                return true;
            }
        }
        return false;
    }

    public boolean dropRightFork() throws Exception {
        if (rightFork != null) {
            if (rightFork.drop(this)) {
                rightFork = null;
                return true;
            }
        }
        return false;
    }

    private int startEating(int i) throws Exception {
        System.out.println("Philosopher " + number + " started eating: " + i);
        sleep(TIME_TO_EAT);
        if (timeInterval == 0) {
            timeInterval = System.currentTimeMillis();
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - timeInterval < TIME_TO_EAT) {
                throw new RuntimeException("it's impossible");
            }
            timeInterval = currentTime;
        }
        return ++i;
    }

    @Override
    public String toString() {
        return "Philosopher " + number + " {" + "left " + leftFork + ", right " + rightFork + "} " + super.toString();
    }
}
