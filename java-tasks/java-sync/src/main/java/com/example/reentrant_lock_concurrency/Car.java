package com.example.reentrant_lock_concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Aleksandr_Savchenko
 */
public class Car {

    private boolean isWashed;
    private Condition isWashedCondition;
    private ReentrantLock lock;

    public Car() {
        this.lock = new ReentrantLock();
        this.isWashedCondition = lock.newCondition();;
    }

    public void startWashing(int i) throws Exception {
        System.out.println("Start washing: " + i);
        Thread.sleep(1000l);
        isWashed = true;
    }

    public void startDrying(int i) throws Exception {
        System.out.println("Start drying: " + i);
        Thread.sleep(1000l);
        isWashed = false;
    }

    public void lockCar() {
        lock.lock();
    }

    public void unlockCar() {
        lock.unlock();
    }

    public boolean isWashed() {
        return isWashed;
    }

    public boolean isNotWashed() {
        return !isWashed;
    }

    public void carIsWaiting() throws InterruptedException {
        isWashedCondition.await();
    }

    public void carIsFree() {
        isWashedCondition.signal();
    }

}
