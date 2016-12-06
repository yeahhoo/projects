package com.example.philosophers_pool;

/**
 * @author Aleksandr_Savchenko
 */
public class Fork {

    private Philosopher owner;
    private int number;

    public Fork(int number) {
        this.number = number;
    }

    public boolean isTaken() {
        return owner != null;
    }

    public synchronized boolean tryTake(Philosopher candidate) throws Exception {
        if (owner == null) {
            owner = candidate;
            return true;
        }
        return false;
    }

    public synchronized boolean drop(Philosopher candidate) throws Exception {
        if (owner == candidate) {
            owner = null;
            return true;
        }
        return false;
    }

    public String getFullInfo() {
        return "Fork {" + number + "}" + " owner: " + owner;
    }

    @Override
    public String toString() {
        return "Fork {" + number + "}";
    }
}
