package com.example.folder_reader_native;

import java.util.concurrent.TimeUnit;

/**
 * @author Aleksandr_Savchenko
 */
public class FolderReaderNativeLauncher {

    public static void main(String[] args) {

        // input params
        String folderToScan = ClassLoader.getSystemResource(".").getPath();
        int numberOfThreads = 4;
        long checkDelay = TimeUnit.SECONDS.toMillis(0l);
        // starting manager
        TaskManager taskManager = new TaskManager(folderToScan, checkDelay);
        taskManager.startTaskManager(numberOfThreads);

    }

}
