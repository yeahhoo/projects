package com.example.reentrant_lock_concurrency;

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
                car.lockCar();
                car.startWashing(i);
                car.carIsFree();
                while (car.isWashed()) {
                    car.carIsWaiting();
                }
                car.unlockCar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
