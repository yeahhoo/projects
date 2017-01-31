package com.example.folder_reader_native;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Aleksandr_Savchenko
 */
public class MyBlockingQueue<T> {

    private List<T> list = new ArrayList<T>();
    private Object isEmptyMonitor = new Object();

    public T getItem() {
        synchronized (isEmptyMonitor) {
            if (list.isEmpty()) {
                try {
                    isEmptyMonitor.wait();
                    return null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                T item = (T) list.get(0);
                list.remove(0);
                return item;
            }
        }
    }

    public void putItem(T item) {
        synchronized (isEmptyMonitor) {
            list.add(item);
            isEmptyMonitor.notify();
        }
    }

    public boolean isEmpty() {
        synchronized (isEmptyMonitor) {
            return list.isEmpty();
        }
    }

    public void releaseThreads(int numberOfThreads) {
        IntStream.range(0, numberOfThreads).forEach(number -> {
            synchronized (isEmptyMonitor) {
                isEmptyMonitor.notifyAll();
            }
        });
    }
}
