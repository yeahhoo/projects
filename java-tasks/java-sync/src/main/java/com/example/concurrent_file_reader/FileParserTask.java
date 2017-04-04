package com.example.concurrent_file_reader;

import com.example.concurrent_task_lib.WorkableItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author Aleksandr_Savchenko
 */
public class FileParserTask implements WorkableItem<Integer> {

    private Position start;
    private Position end;
    private RandomAccessFile raf;

    FileParserTask(File file, Position start, Position end) throws FileNotFoundException {
        this.raf = new RandomAccessFile(file, "r");
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer processItem() {
        try {
            gaugePosition(start, false);
            gaugePosition(end, true);
            int sum = 0;
            if (end.getPosition() > start.getPosition()) {
                String result = readInterval();
                if (result != null && !"".equals(result)) {
                    sum = Arrays.asList(result.split(",")).stream().map(s -> Integer.parseInt(s)).reduce(0, (x, y) -> x + y);
                }
            }
            return sum;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while reading file", e);
        } finally {
            try {
                raf.close();
            } catch (IOException e1) {
                throw new RuntimeException("Error occurred while closing RandomAccessFile", e1);
            }
        }
    }

    private String readInterval() throws IOException {
        int startPosition = (int) start.getPosition();
        if (startPosition > 0) {
            startPosition++;
        }
        int length = (int) (end.getPosition() - startPosition);
        byte[] bytes = new byte[length];
        raf.seek(startPosition);
        raf.read(bytes, 0, length);

        String str = new String(bytes, Charset.forName("UTF-8"));
        str = postCleaning(str);
        return str;
    }

    private void gaugePosition(Position position, boolean moveForward) throws Exception {
        long cursor = position.getPosition();
        while (!position.isGauged()) {
            raf.seek(cursor);
            int r = raf.read();
            if (isBorderFound(r)) {
                position.setPosition(cursor);
                return;
            }
            if (moveForward) {
                cursor++;
            } else {
                cursor--;
            }
        }
    }

    // must be overridden
    private String postCleaning(String str) {
        return str.replaceAll("\\r\\n", ",").replaceAll("\\n", "").replaceAll("\\r", "");
    }

    // must be overridden
    private boolean isBorderFound(int r) {
        return r == -1 || (char) r == '\r' || (char) r == '\n'; // int 10 and 13 respectively - just for keeping code clean
    }

}
