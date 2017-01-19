package com.example.folder_reader_native;


import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Aleksandr_Savchenko
 */
public class DataStorage {

    private static final Map<File, String> resultMap = new LinkedHashMap<>();
    private static final Queue<File> taskQueue = new LinkedBlockingQueue<>();
    // double check to be sure no tasks were added to queue after it returned empty status
    private static volatile Boolean isEmptyDoubleCheck = Boolean.FALSE;

    public static void addResult(File file, String threadName) {
        synchronized (resultMap) {
            resultMap.put(file, threadName);
        }
    }

    public static void printResults() {
        synchronized (resultMap) {
            resultMap.entrySet().stream().forEach((Map.Entry<File, String> entry) -> {
                System.out.println(String.format("%s processed by thread: %s", entry.getKey(), entry.getValue()));
            });
            System.out.println(String.format("processed %d entries", resultMap.size()));
        }
    }

    public static void addTask(File folder) {
        synchronized (taskQueue) {
            taskQueue.add(folder);
        }
        synchronized (isEmptyDoubleCheck) {
            if (isEmptyDoubleCheck) {
                isEmptyDoubleCheck = Boolean.FALSE;
            }
        }
    }

    public static File getTask() {
        synchronized (taskQueue) {
            return taskQueue.poll();
        }
    }

    public static boolean isTaskQueueEmpty() {
        boolean isEmpty = false;
        synchronized (taskQueue) {
            isEmpty = taskQueue.isEmpty();
        }
        if (isEmpty) {
            synchronized (isEmptyDoubleCheck) {
                isEmptyDoubleCheck = Boolean.TRUE;
            }
        }
        return isEmpty;
    }

    public static boolean isTaskQueueEmptyDoubleCheck() {
        synchronized (isEmptyDoubleCheck) {
            return isEmptyDoubleCheck;
        }
    }

}
