package com.example.reentrant_lock_concurrency;

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
                car.lockCar();
                while (car.isNotWashed()) {
                    car.carIsWaiting();
                }
                car.startDrying(i);
                car.carIsFree();
                car.unlockCar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
