package com.example.philosophers_pool;

/**
 * @author Aleksandr_Savchenko
 */
public class Fork {

    private int number;

    private boolean taken;

    public Fork(int number) {
        this.number = number;
    }

    public synchronized boolean tryTake() throws Exception {
        if (!taken) {
            taken = true;
            return true;
        }
        return false;
    }

    public synchronized boolean drop() throws Exception {
        if (taken) {
            taken = false;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Fork {" + number + "}";
    }
}
