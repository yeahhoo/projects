package com.example.folder_reader_native;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Aleksandr_Savchenko
 */
public class TaskManager implements Runnable {

    private List<SearchTask> threads = new ArrayList<>();
    private String folderToScan;
    private long checkDelay;
    private int numberOfThreads;

    public TaskManager(String folderToScan, int numberOfThreads, long checkDelay) {
        this.folderToScan = folderToScan;
        this.numberOfThreads = numberOfThreads;
        this.checkDelay = checkDelay;
    }

    @Override
    public void run() {
        initTasks(numberOfThreads);
        while (true) {
            if (isFinished()) {
                finishTasks();
                DataStorage.printResults();
                return;
            }
            try {
                Thread.sleep(checkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                finishTasks();
            }
        }
    }

    /**
     * The algorithm uses double checking to be sure that no tasks were added while iterating through all threads and getting their statuses.
     * Steps of the algorithm:
     * 1) Checking if the queue is empty;
     * 2) Checking that all threads aren't doing any processing;
     * 3) Double checking on none of tasks were added while iterating.
     * */
    private boolean isFinished() {
        if (DataStorage.isTaskQueueEmpty()) {
            boolean isAllThreadsFinished = threads.stream().map(task -> task.isWorking()).allMatch(o -> o == false);
            if (isAllThreadsFinished) {
                return DataStorage.isTaskQueueEmptyDoubleCheck();
            }
        }
        return false;
    }

    private void initTasks(int numberOfThreads) {
        DataStorage.addTask(new File(folderToScan));
        IntStream.range(0, numberOfThreads).forEach(i -> {
            SearchTask searchTask = new SearchTask("thread" + i);
            threads.add(searchTask);
            Thread thread = new Thread(searchTask);
            thread.start();
        });
    }

    private void finishTasks() {
        threads.stream().forEach(task -> task.finishJob());
        DataStorage.freeThreads(numberOfThreads);
    }

}
