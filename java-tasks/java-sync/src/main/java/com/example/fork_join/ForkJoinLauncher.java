package com.example.fork_join;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Aleksandr_Savchenko
 */
public class ForkJoinLauncher {

    public static void main(String[] args) {

        String folderToScan = ClassLoader.getSystemResource(".").getPath();//"D:/projects/java-tasks/java-sync/src/main/java/com/example"

        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        FileScannerTask task = new FileScannerTask(folderToScan);
        List<String> paths = forkJoinPool.invoke(task);
        task.join();
        forkJoinPool.shutdown();
        paths.stream().forEach(System.out::println);
    }

}
