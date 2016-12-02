package wait_notify_concurrency;


/**
 * @author Aleksandr_Savchenko
 */
public class Car {

    private boolean isWashed;

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

    public boolean isWashed() {
        return isWashed;
    }

    public boolean isNotWashed() {
        return !isWashed;
    }
}
