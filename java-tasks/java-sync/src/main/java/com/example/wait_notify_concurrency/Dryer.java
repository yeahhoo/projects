package com.example.wait_notify_concurrency;

/**
 * @author Aleksandr_Savchenko
 */
public class Dryer implements Runnable {

    private Car car;

    public Dryer(Car car) {
        this.car = car;
    }

    @Override
    public void run() {
        System.out.println("Dryer started");
        try {
            for (int i = 0; i < 5; i++) {
                synchronized (car) {
                    while (car.isNotWashed()) {
                        car.wait();
                    }
                    car.startDrying(i);
                    car.notify();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
