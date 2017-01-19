package com.example.folder_reader_native;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksandr_Savchenko
 */
public class TaskManager {

    private List<SearchTask> threads = new ArrayList<>();
    private String folderToScan;
    private boolean isFinished = false;
    private long checkDelay;

    public TaskManager(String folderToScan, long checkDelay) {
        this.folderToScan = folderToScan;
        this.checkDelay = checkDelay;
    }

    public void startTaskManager(int numberOfThreads) {
        initTasks(numberOfThreads);
        while (true) {
            // double check on all threads finished their jobs and there were no tasks added between iteration
            if (DataStorage.isTaskQueueEmpty()) {
                boolean isAllThreadsFinished = threads.stream().map(task -> task.isWorking()).allMatch(o -> o == false);
                if (isAllThreadsFinished) {
                    isFinished = DataStorage.isTaskQueueEmptyDoubleCheck();
                }
            }

            if (isFinished) {
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

    private void initTasks(int numberOfThreads) {
        synchronized (this) {
            DataStorage.addTask(new File(folderToScan));
        }
        for (int i = 0; i < numberOfThreads; i++) {
            SearchTask searchTask = new SearchTask("thread" + i);
            threads.add(searchTask);
            Thread thread = new Thread(searchTask);
            thread.start();
        }
    }

    private void finishTasks() {
        threads.stream().forEach(task -> task.finishJob());
    }

}
