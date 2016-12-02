package reentrant_lock_concurrency;

/**
 * @author Aleksandr_Savchenko
 */
public class CarWashingReentrantLockLauncher {


    public static void main(String[] args) {
        Car car = new Car();
        new Thread(new Washer(car)).start();
        new Thread(new Dryer(car)).start();
    }

}
