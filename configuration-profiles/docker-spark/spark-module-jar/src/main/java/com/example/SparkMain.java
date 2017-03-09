package com.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * @author Aleksandr_Savchenko
 */
public class SparkMain {

    public static void main(String[] args) throws Exception {
        //http://192.168.99.100
        wordCountJava8("hdfs://namenode:8020/test1.txt", "hdfs://namenode:8020/output");
    }

    //https://community.hortonworks.com/questions/23976/vonnect-hdp-24-spark-remotely-failed.html
    public static void wordCountJava8(String filename, String outputFolder) {
        // Define a configuration to use to interact with Spark
        //SparkConf conf = new SparkConf().setMaster("spark://192.168.99.100:7077").setAppName("Work Count App"); // spark://spark-master:7077 || spark://192.168.99.100
        //conf.set("spark.executor.memory", "128m");

        SparkConf conf = new SparkConf()
                .setMaster("spark://spark-master:7077")
                .setAppName("My Work Count App");
                //.set("spark.driver.host", "spark-master");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load the input data, which is a text file read from the command line
        JavaRDD<String> input = sc.textFile( filename );

        // Java 8 with lambdas: split the input string into words
        JavaRDD<String> words = input.flatMap(s -> Arrays.asList(s.split( " " )).iterator());
        JavaPairRDD<String, Integer> counts = words.mapToPair(t -> new Tuple2(t, 1 )).reduceByKey((x, y) -> (int) x + (int) y);
        counts.saveAsTextFile(outputFolder);

        sc.stop();
    }

}
