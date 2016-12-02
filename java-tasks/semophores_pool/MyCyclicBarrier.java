package semophores_pool;

import java.util.concurrent.CyclicBarrier;

/**
 * @author Aleksandr_Savchenko
 */
public class MyCyclicBarrier {

    private CyclicBarrier cyclicBarrier;

    public MyCyclicBarrier(int threadNumber) {
        cyclicBarrier = new CyclicBarrier(threadNumber);
    }

    public MyCyclicBarrier(int threadNumber, Runnable action) {
        cyclicBarrier = new CyclicBarrier(threadNumber, action);
    }

    public void await() {
        try {
            cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
