package com.example;

import com.mongodb.spark.MongoSpark;

import org.apache.spark.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * @author Aleksandr_Savchenko
 */
public class SparkMain {

    public static void main(String[] args) throws Exception {
        runStreaming();
    }

    public static void runStreaming() throws InterruptedException {
        // Define a configuration to use to interact with Spark
        SparkConf conf = new SparkConf()
                .setMaster("spark://spark-master:7077")
                .setAppName("My Streaming Count App")
                .set("spark.mongodb.output.uri", "mongodb://mongo-host:27017/")
                .set("spark.mongodb.output.database", "local")
                .set("spark.mongodb.output.collection", "spark")
                //.set("spark.executor.memory", "1024m")
                //.set("spark.cores.max", "1")
                //.set("spark.default.parallelism", "3")
                ;
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        jssc.checkpoint(".");
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
        /*
        // printing results in nice view
        sortedStream.map(new Function<Tuple2<Integer, Integer>, String>() {
            @Override
            public String call(Tuple2<Integer, Integer> tupple) throws Exception {
                String template = "(%d .. %d, %d)";
                return String.format(template, tupple._1, tupple._1 + 10, tupple._2);
            }
        }).print();
        */


        /*
        // collecting overall statistics, don't forget to set-up shared FS (HDFS or S3)
        JavaPairDStream<Integer, Integer> sumStream = sortedStream.updateStateByKey(
                new Function2<List<Integer>, Optional<Integer>, Optional<Integer>>() {
                    @Override
                    public Optional<Integer> call(List<Integer> integers, Optional<Integer> current) throws Exception {
                        int currentSum = current.or(0) + integers.stream().reduce(0, (x, y) -> x + y);
                        return Optional.of(currentSum);
                    }
                });

        */


        sortedStream.foreachRDD(new VoidFunction2<JavaPairRDD<Integer, Integer>, Time>() {
            @Override
            public void call(JavaPairRDD<Integer, Integer> integerIntegerJavaPairRDD, Time time) throws Exception {
                if (!integerIntegerJavaPairRDD.isEmpty()) {

                    JavaRDD<Document> rdd = integerIntegerJavaPairRDD.map(new Function<Tuple2<Integer, Integer>, Document>() {
                        @Override
                        public Document call(Tuple2<Integer, Integer> tupple) throws Exception {
                            return new Document(String.valueOf(tupple._1), tupple._2);
                        }
                    });


                    Document doc = new Document("_id", new ObjectId(new Date(time.milliseconds())));
                    Document reduced = rdd.reduce(new Function2<Document, Document, Document>() {
                        @Override
                        public Document call(Document document, Document document2) throws Exception {
                            // merging, didn't find any better way for that
                            document2.keySet().stream().forEach(key -> {
                                document.put(key, document2.get(key));
                            });
                            return document;
                        }
                    });
                    doc.append("list", reduced);

                    List<Document> list = Collections.singletonList(doc);
                    JavaRDD<Document> r = JavaSparkContext.fromSparkContext(integerIntegerJavaPairRDD.context()).parallelize(list);
                    MongoSpark.save(r);
                    //integerIntegerJavaPairRDD.coalesce(1).saveAsTextFile("/mydata");
                }
            }
        });

        jssc.start();              // Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
    }

}
