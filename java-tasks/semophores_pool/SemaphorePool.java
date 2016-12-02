package semophores_pool;

import java.util.concurrent.Semaphore;

/**
 * Created by Aleksandr_Savchenko on 7/25/2016.
 */
public class SemaphorePool {

    private Semaphore semaphore;

    public SemaphorePool(int semaphoreCapacity) {
        semaphore = new Semaphore(semaphoreCapacity);
    }

    public boolean takeConnect(WorkerThread thread, int i) {
        try {
            semaphore.acquire(1);
            thread.doJob(i);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            semaphore.release(1);
        }
    }
}
