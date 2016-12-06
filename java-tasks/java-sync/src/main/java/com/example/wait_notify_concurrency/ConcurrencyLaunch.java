package com.example.wait_notify_concurrency;

/**
 * @author Aleksandr_Savchenko
 */
public class ConcurrencyLaunch {

    public static void main(String[] args) {
        Car car = new Car();

        new Thread(new Dryer(car)).start();
        new Thread(new Washer(car)).start();
    }

}