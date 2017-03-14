#!/bin/bash

echo "uploading data to HDFS namenode"
hdfs dfs -put /mydata/test1.txt  hdfs://namenode:8020/
hdfs dfs -put /mydata/test2.txt  hdfs://namenode:8020/
echo "files uploaded"