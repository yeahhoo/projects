package com.example;

import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Aleksandr_Savchenko
 */
public class SparkMain {

    public static void main(String[] args) throws Exception {
        wordCountJava8();
    }

    public static void wordCountJava8() throws InterruptedException {
        // Define a configuration to use to interact with Spark
        SparkConf conf = new SparkConf()
                .setMaster("spark://spark-master:7077")
                .setAppName("My Streaming Count App")
                //.set("spark.executor.memory", "1024m")
                //.set("spark.cores.max", "1")
                //.set("spark.default.parallelism", "3")
                ;
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("web-app", 9999);
        JavaDStream<String> words = lines.flatMap(
                new FlatMapFunction<String, String>() {
                    @Override
                    public Iterator<String> call(String x) {
                        return Arrays.asList(x.split(" ")).iterator();
                    }
                });

        JavaPairDStream<String, Integer> pairs = words.mapToPair(
                new PairFunction<String, String, Integer>() {
                    @Override
                    public Tuple2<String, Integer> call(String s) {
                        return new Tuple2<>(s, 1);
                    }
                });
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer i1, Integer i2) {
                        return i1 + i2;
                    }
                });

        // Print the first ten elements of each RDD generated in this DStream to the console
        wordCounts.print();
        jssc.start();              // Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
    }


}
