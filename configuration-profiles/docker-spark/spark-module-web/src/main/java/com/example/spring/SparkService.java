package com.example.spring;

import com.example.SparkMain;

import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
@Service
public class SparkService {

    @Value("${hdfs.url}")
    private String hdfs;

    @Autowired
    private JavaSparkContext sc;

    public Map<String, Integer> analyzeData(String inputFile, String outputFolder) {
        String inputPath = hdfs + inputFile;
        String outputPath = hdfs + outputFolder;
        return SparkMain.calculate(sc, inputPath, outputPath);
    }

}
