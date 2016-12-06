package com.example.wait_notify_concurrency;

/**
 * @author Aleksandr_Savchenko
 */
public class Washer implements Runnable {

    private Car car;

    public Washer(Car car) {
        this.car = car;
    }

    @Override
    public void run() {
        System.out.println("Washer started");
        try {
            for (int i = 0; i < 5; i++) {
                synchronized (car) {
                    car.startWashing(i);
                    car.notify();
                    while (car.isWashed()) {
                        car.wait();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
