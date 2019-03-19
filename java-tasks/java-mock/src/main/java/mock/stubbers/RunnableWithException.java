package mock.stubbers;

/** Differs from {@link Runnable} only in that this class throws exception. */
public interface RunnableWithException {

    /** {@link Runnable#run()} that throws exception. */
    void run() throws Throwable;
}
