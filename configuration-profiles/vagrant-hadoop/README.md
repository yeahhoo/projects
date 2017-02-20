**Vagrant Hadoop config**

This instruction creates Vagrant with installed Hadoop instance. Follow the below steps to have it:

```sh
cd /vagrant-hadoop
mvn clean install
cd /vagrant-hadoop/vagrant
vagrant up
```

Check that hadoop installed and configured, to do that SSH to a host and connect to vagrant host:

if you want to execute job in pseudo-distributed mode

```sh
1) reach the url and check that hadoop is up and running: http://localhost:50070/explorer.html/
2) vagrant ssh hadoop
3) cd /
4) check that java -version and hadoop version are available system wide
5) hdfs dfs -put /vagrant/resources/test.txt /
6) hadoop jar /mvn-target/test-wordcount.jar /test.txt /output
7) hadoop fs -cat /output/part-r-00000
8) check that the file successfully proceeded: http://localhost:50070/explorer.html#/output
9) remove HDFS file: hdfs dfs -rm -r  /output
```

if you want to execute job in spring-boot (standalone mode)

```sh
1) reach the url and check that hadoop is up and running: http://localhost:50070/explorer.html/
2) vagrant ssh hadoop
3) cd /
4) check that java -version and hadoop version are available system wide
5) hdfs dfs -put /vagrant/resources/test.txt /
6) hit the url to check if application is ready: GET http://localhost:11091/server/test
7) hit the url to execute job: GET http://localhost:11091/server/runJob
8) remove HDFS file: hdfs dfs -rm -r  /output
```

To clean resources run the following commands:

```sh
vagrant halt
vagrant destroy
```

for debug of your application run the following command:
```sh
10.211.55.101:8081 - address to debug spring-boot application
/vagrant/resources/debug-hadoop /mvn-target/test-wordcount.jar /test.txt /output
10.211.55.101:17412 - address to debug hadoop core components
10.211.55.101:17414 - address to debug hadoop mappers and reducers (please note it available only at map/reduce stage because suspend=n)
```


**Useful Commands:**
```sh
netstat -atn
vmstat 1
hadoop fs -cat /output/part-r-00000
hadoop fs -ls hdfs://localhost:8020/
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8081 test-wordcount.jar
```

**Sources:**

https://github.com/vangj/vagrant-hadoop-2.3.0

https://dzone.com/articles/effective-testing-strategies

http://stayrelevant.globant.com/debug-your-mapreduce-program-locally/

https://pravinchavan.wordpress.com/2013/04/05/remote-debugging-of-hadoop-job-with-eclipse/

https://github.com/tequalsme/hadoop-examples

http://protocolsofmatrix.blogspot.ru/2016/09/install-hadoop-273-on-ubuntu-1604-lts.html

http://www.codingpedia.org/ama/spring-batch-tutorial-with-spring-boot-and-java-configuration/
http://codecentric.github.io/spring-boot-starter-batch-web/#_getting_started
http://blog.hexican.com/2014/07/a-spring-boot-rest-wrapper/