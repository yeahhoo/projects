package com.example.fork_join;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author Aleksandr_Savchenko
 */
public class FileScannerTask extends RecursiveTask<List<String>> {

    private String folderToScan;

    public FileScannerTask(String folderToScan) {
        this.folderToScan = folderToScan;
    }

    @Override
    protected List<String> compute() {
        List<String> files = new ArrayList<String>();
        List<FileScannerTask> tasks = new ArrayList<FileScannerTask>();

        File rootFolder = new File(folderToScan);
        Arrays.asList(rootFolder.listFiles()).stream().forEach((File file) -> {
            if (file.isDirectory()) {
                FileScannerTask subTask = new FileScannerTask(file.getAbsolutePath());
                tasks.add(subTask);
                files.add(file.getAbsolutePath());
                subTask.fork();
            } else {
                files.add(file.getAbsolutePath());
            }
        });

        tasks.stream().forEach(task -> {
            files.addAll(task.join());
        });

        return files;
    }
}
