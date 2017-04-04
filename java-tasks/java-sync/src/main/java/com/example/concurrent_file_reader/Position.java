package com.example.concurrent_file_reader;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Aleksandr_Savchenko
 */
public class Position {

    private volatile long position;
    private volatile AtomicBoolean isGauged = new AtomicBoolean(false);

    public Position(long position) {
        this.position = position;
    }

    public long getPosition() {
        return position;
    }

    public boolean setPosition(long position) {
        if (isGauged.compareAndSet(false, true)) {
            this.position = position;
            return true;
        }
        return false;
    }

    public boolean isGauged() {
        return isGauged.get();
    }

    @Override
    public String toString() {
        return "Position{" +
               "position=" + position +
               '}';
    }
}
