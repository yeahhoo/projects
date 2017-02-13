**Vagrant Hadoop config**

This instruction creates Vagrant with installed Hadoop instance. Follow the below steps to have it:

```sh
cd /vagrant-hadoop
mvn clean install
cd /vagrant-hadoop/vagrant
vagrant up
```

Check that hadoop installed and configured, to do that SSH to a host and connect to vagrant host:

```sh
1) reach the url and check that hadoop is up and running: http://localhost:50070/explorer.html/
2) vagrant ssh hadoop
3) cd /
4) check that java -version and hadoop version are available system wide
5) hdfs dfs -put /vagrant/resources/test.txt /
6) hadoop jar /mvn-target/test-wordcount.jar /test.txt /output
7) hadoop fs -cat /output/part-r-00000
8) check that the file successfully proceeded: http://localhost:50070/explorer.html#/output
```

To clean resources run the following commands:

```sh
vagrant halt
vagrant destroy
```

**Sources:**

https://github.com/vangj/vagrant-hadoop-2.3.0

http://stayrelevant.globant.com/debug-your-mapreduce-program-locally/

https://github.com/tequalsme/hadoop-examples

