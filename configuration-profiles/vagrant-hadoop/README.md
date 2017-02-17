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
9) remove HDFS file: hdfs dfs -rm -r  /output
```

To clean resources run the following commands:

```sh
vagrant halt
vagrant destroy
```

**Debug:**

for debug of your application run the following command:
```sh
/vagrant/resources/debug-hadoop /mvn-target/test-wordcount.jar /test.txt /output
10.211.55.101:17412 - address to debug hadoop core components
10.211.55.101:17414 - address to debug hadoop mappers and reducers (please note it available only at map/reduce stage because suspend=n)
```


**Useful Commands:**
```sh
netstat -atn
vmstat 1
```

**Sources:**

https://github.com/vangj/vagrant-hadoop-2.3.0

https://dzone.com/articles/effective-testing-strategies

http://stayrelevant.globant.com/debug-your-mapreduce-program-locally/

https://pravinchavan.wordpress.com/2013/04/05/remote-debugging-of-hadoop-job-with-eclipse/

https://github.com/tequalsme/hadoop-examples

http://protocolsofmatrix.blogspot.ru/2016/09/install-hadoop-273-on-ubuntu-1604-lts.html

