package com.example.dependency_resolver.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * it does job similar to parallelStream(). Implemented just for fun.
 *
 * @author Aleksandr_Savchenko
 */
public class ConcurrentListProcessor<Res> {

    private Collection<? extends WorkableItem> workList;
    private ExecutorService service;

    public ConcurrentListProcessor(Collection<? extends WorkableItem> workList, int numberOfThreads) {
        this.workList = workList;
        service = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void processCollectionConsumingInParallel(Consumer<Res> consumer) throws InterruptedException, ExecutionException {
        ExecutorCompletionService<Res> executor = new ExecutorCompletionService<>(service);
        for (WorkableItem item : workList) {
            executor.submit(new ConcurrentTask(item));
        }
        for (int i = 0; i < workList.size(); i++) {
            Future<Res> f = executor.take();
            Res res = f.get();
            consumer.accept(res);
        }
        service.shutdownNow();
    }

    public List<Res> processCollection() throws InterruptedException, ExecutionException {
        List<Res> resultList = new ArrayList<>();
        ExecutorCompletionService<Res> executor = new ExecutorCompletionService<>(service);
        for (WorkableItem item : workList) {
            executor.submit(new ConcurrentTask(item));
        }
        for (int i = 0; i < workList.size(); i++) {
            Future<Res> f = executor.take();
            Res res = f.get();
            resultList.add(res);
        }
        service.shutdownNow();
        return resultList;
    }

}
