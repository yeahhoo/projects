package com.example;

import org.apache.spark.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

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
        JavaReceiverInputDStream<String> numbers = jssc.socketTextStream("web-app", 9999);

        JavaPairDStream<Integer, Integer> pairs = numbers.mapToPair(
                new PairFunction<String, Integer, Integer>() {
                    @Override
                    public Tuple2<Integer, Integer> call(String s) {
                        return new Tuple2<>((Integer.parseInt(s) / 10) * 10, 1); // rounding to 10
                    }
                });

        JavaPairDStream<Integer, Integer> wordCounts = pairs.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer i1, Integer i2) {
                        return i1 + i2;
                    }
                });

        JavaPairDStream<Integer, Integer> sortedStream = wordCounts.transformToPair(
                new Function<JavaPairRDD<Integer, Integer>, JavaPairRDD<Integer, Integer>>() {
                    @Override
                    public JavaPairRDD<Integer, Integer> call(JavaPairRDD<Integer, Integer> integerIntegerJavaPairRDD) throws Exception {
                        return integerIntegerJavaPairRDD.sortByKey(true);
                    }
                });

        sortedStream.map(new Function<Tuple2<Integer,Integer>, String>() {
            @Override
            public String call(Tuple2<Integer, Integer> tupple) throws Exception {
                String template = "(%d .. %d, %d)";
                return String.format(template, tupple._1, tupple._1 + 10, tupple._2);
            }
        }).print();

        jssc.start();              // Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
    }


}
