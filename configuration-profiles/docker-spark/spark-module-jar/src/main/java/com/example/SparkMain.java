package com.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
public class SparkMain {

    public static void main(String[] args) throws Exception {
        Map<String, Integer> result = wordCountJava8("hdfs://namenode:8020/test1.txt", "hdfs://namenode:8020/output");
        result.forEach((String k, Integer v)-> System.out.println(k + " / " + v));
    }

    public static Map<String, Integer> wordCountJava8(String filename, String outputFolder) {
        // Define a configuration to use to interact with Spark
        SparkConf conf = new SparkConf()
                .setMaster("spark://spark-master:7077")
                .setAppName("My Work Count App")
                //.set("spark.executor.memory", "1024m")
                //.set("spark.cores.max", "1")
                //.set("spark.default.parallelism", "3")
                ;
        JavaSparkContext sc = new JavaSparkContext(conf);
        Map<String, Integer> result = calculate(sc, filename, outputFolder);
        sc.stop();
        return result;
    }


    public static Map<String, Integer> calculate(JavaSparkContext sc, String filename, String outputFolder) {
        // Load the input data, which is a text file read from the command line
        JavaRDD<String> input = sc.textFile(filename);
        // Java 8 with lambdas: split the input string into words
        JavaRDD<String> words = input.flatMap(s -> Arrays.asList(s.split( " " )).iterator());
        JavaPairRDD<String, Integer> counts = words.mapToPair(t -> new Tuple2(t, 1 )).reduceByKey((x, y) -> (int) x + (int) y);
        counts.saveAsTextFile(outputFolder);
        Map<String, Integer> result = counts.collectAsMap();
        return result;
    }

}
