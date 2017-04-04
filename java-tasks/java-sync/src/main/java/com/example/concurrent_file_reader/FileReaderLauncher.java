package com.example.concurrent_file_reader;


import com.example.concurrent_task_lib.ConcurrentListProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of file concurrent reader. Obviously it's faster to read file sequentially in one thread
 * but it can be good for reading of replicated file by a few JVMs.
 * @author Aleksandr_Savchenko
 */
public class FileReaderLauncher {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        System.out.println("Starting reading file");
        int numberOfThreads = 3;
        List<FileParserTask> workList = new ArrayList<>(numberOfThreads);
        // initial gauge
        File file = new File(ClassLoader.getSystemResource("./test.csv").getPath());
        long fileLength = file.length();
        Position previousPosition = new Position(0);
        previousPosition.setPosition(0);
        for (int i = 1; i <= numberOfThreads; i++) {
            long end = (i) * fileLength / numberOfThreads;
            Position endPosition = new Position(end);
            FileParserTask task = new FileParserTask(file, previousPosition, endPosition);
            workList.add(task);
            previousPosition = endPosition;
        }

        // concurrent processing
        ConcurrentListProcessor<Integer> processor = new ConcurrentListProcessor<>(workList, numberOfThreads);
        List<Integer> results = processor.processCollection();
        int sum = results.stream().reduce(0, (x, y) -> x + y);
        assert 3250 == sum;
        System.out.println("sums equal, success");
    }

}
