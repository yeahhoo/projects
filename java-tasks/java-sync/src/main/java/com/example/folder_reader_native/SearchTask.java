package com.example.folder_reader_native;

import java.io.File;
import java.util.Arrays;

/**
 * @author Aleksandr_Savchenko
 */
public class SearchTask implements Runnable {

    private String taskName;
    private volatile boolean isWorking = false;
    private boolean isFinished = false;
    private volatile Object monitor = new Object();

    public SearchTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        while (!isFinished) {
            File folderToProcess = null;
            synchronized (monitor) {
                folderToProcess = DataStorage.getTask();
                if (folderToProcess != null) {
                    setWorkingStatus(true);
                }
            }
            if (folderToProcess != null) {
                processFolder(folderToProcess);
            }
        }
    }

    public void finishJob() {
        isFinished = true;
    }

    public boolean isWorking() {
        synchronized (monitor) {
            return isWorking;
        }
    }

    private void setWorkingStatus(boolean isWorking) {
        synchronized (monitor) {
            this.isWorking = isWorking;
        }
    }

    private void processFolder(File folder) {
        // it's better to add files and folders by batch but here it's implemented iteratively just to provide higher concurrency level (a lot of context switches).
        Arrays.asList(folder.listFiles()).stream().forEach((File file) -> {
            if (file.isDirectory()) {
                DataStorage.addTask(file);
            } else {
                DataStorage.addResult(file, taskName);
            }
        });
        setWorkingStatus(false);
    }
}
