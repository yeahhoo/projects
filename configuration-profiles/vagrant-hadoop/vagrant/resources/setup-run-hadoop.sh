#!/bin/bash

HADOOP_VERSION=hadoop-2.7.3

echo "creating hadoop directories"
mkdir /tmp/hadoop-namenode
mkdir /tmp/hadoop-logs
mkdir /tmp/hadoop-datanode

echo "generating hadoop keys private rsa keys for ssh localhost"
/vagrant/resources/create-private-keys.sh

echo "modifying permissions on local file system"
chown -fR hadoop /tmp/hadoop-namenode
chown -fR hadoop /tmp/hadoop-logs
chown -fR hadoop /tmp/hadoop-datanode

echo "setting up namenode"
/usr/local/${HADOOP_VERSION}/bin/hdfs namenode -format myhadoop

echo "starting hadoop service"
service hadoop-service start	### todo change it to 2.7.3 version & fix nohup command

# init HDFS temporary directory
/usr/local/hadoop/bin/hdfs --config /usr/local/hadoop/etc/hadoop dfs -mkdir /tmp
/usr/local/hadoop/bin/hdfs --config /usr/local/hadoop/etc/hadoop dfs -chmod -R 777 /tmp

/vagrant/resources/run-spring-app.sh
