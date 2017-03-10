package com.example.spring;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aleksandr_Savchenko
 */
@Configuration
public class SparkConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkConfig.class);

    @Value("${spark.master}")
    private String master;

    @Value("${jar.location.countword}")
    private String jarLocation;

    @Bean
    public JavaSparkContext javaSparkContext() {
        LOGGER.info("Creating SparkContext.  Master = " + master);
        SparkConf conf = new SparkConf()
                .setAppName("Spark Spring App")
                .setMaster(master)
                .setJars(new String[] {jarLocation});
                //.set("spark.executor.memory", "128m")
                //.set("spark.cores.max", "1")
                //.set("spark.default.parallelism", "3")
                ;
        return new JavaSparkContext(conf);
    }

}
