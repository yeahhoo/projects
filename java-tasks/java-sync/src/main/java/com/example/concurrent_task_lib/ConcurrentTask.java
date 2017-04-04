package com.example.concurrent_task_lib;

import java.util.concurrent.Callable;

/**
 * @author Aleksandr_Savchenko
 */
public class ConcurrentTask<T> implements Callable {

    private WorkableItem item;

    public ConcurrentTask(WorkableItem item) {
        this.item = item;
    }

    @Override
    public T call() throws Exception {
        return (T) item.processItem();
    }
}
