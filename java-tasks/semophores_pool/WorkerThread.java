package semophores_pool;

/**
 * @author Aleksandr_Savchenko
 */
public class WorkerThread implements Runnable {

    private int id;
    private long executingTime;
    private SemaphorePool pool;
    private MyCyclicBarrier barrier;


    public WorkerThread(int id, SemaphorePool pool, long executingTime, MyCyclicBarrier barrier) {
        this.id = id;
        this.pool = pool;
        this.executingTime = executingTime;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        System.out.println("GETTING STARTED");
        barrier.await();
        int i = 0;
        while (i < 5) {
            try {
                if (pool.takeConnect(this, i)) {
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        barrier.await();
    }

    public void doJob(int i) throws Exception {
        System.out.println(String.format("Thread %d started doing job iteration %d", id, i));
        Thread.sleep(executingTime);
    }

}
