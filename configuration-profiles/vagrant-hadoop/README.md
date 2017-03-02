**Vagrant Hadoop config**

This instruction creates Vagrant with installed Hadoop instance. The stack is: Hadoop, Spring Boot, Spring Batch. Follow the below steps to have it:

```sh
cd /vagrant-hadoop
mvn clean install
cd /vagrant-hadoop/vagrant
vagrant up
add redirecting rule to %WinDir%\System32\Drivers\Etc\hosts file or /etc/hosts file:
10.211.55.101		hadoop-yarn
```

The script comes with 2 modes (standalone jar and spring-boot app) so pick the mode actual for you.

Please note that all operations on Hadoop must be executed on behalf of hadoop/hadoop user.

if you want to execute job by standalone jar

```sh
1) reach the url and check that hadoop is up and running: http://localhost:50070/explorer.html/
2) vagrant ssh hadoop
3) cd /
4) su - hadoop (password hadoop)
5) check that java -version and hadoop version are available system wide
6) check that SSH connect is set-up:
ssh localhost (asked without prompting password)
exit
7) hdfs dfs -put /vagrant/resources/test.txt /
8) hadoop jar /mvn-target/map-reduce-module.jar /test.txt /output
9) hadoop fs -cat /output/part-r-00000
10) check that the file successfully proceeded: http://localhost:50070/explorer.html#/output
11) remove HDFS file: hdfs dfs -rm -r  /output
12) change user back: su - vagrant (password vagrant)
```

**Debug standalone jar**

```sh
/vagrant/resources/debug-hadoop /mvn-target/map-reduce-module.jar /test.txt /output
10.211.55.101:17412 - address to debug hadoop core components
10.211.55.101:17414 - address to debug hadoop mappers and reducers (please note it available only at map/reduce stage because suspend=n)
```

if you want to execute job in spring-boot

```sh
1) reach the url and check that hadoop is up and running: http://localhost:50070/explorer.html/
2) vagrant ssh hadoop
3) cd /
4) switch to user hadoop: su - hadoop (password hadoop)
5) check that java -version and hadoop version are available system wide
6) check that SSH connect is set-up:
ssh localhost (asked without prompting password)
exit
7) hdfs dfs -put /vagrant/resources/test.txt /
8) hit the url to check if application is ready: GET http://localhost:11091/server/test
9) hit the url to execute job: GET http://localhost:11091/server/runJob?isAsync=false&isDebug=true
10) remove HDFS file: hdfs dfs -rm -r  /output
11) change user back: su - vagrant (password vagrant)
```

**Debug spring-boot**

```sh
run job with HTTP GET param isDebug=true, example: http://localhost:11091/server/runJob?isAsync=false&isDebug=true
Please note that in this case the job won't appear in cluster job list.
10.211.55.101:8081 - address to debug spring-boot application
```

To clean resources run the following commands:

```sh
vagrant halt
vagrant destroy
```

**Logging:**

You can monitor logs here: http://localhost:19888/logs/

You can watch all jobs here: http://localhost:8088/cluster/apps


**Useful Commands:**
```sh
netstat -atn
vmstat 1
hadoop fs -cat /output/part-r-00000
hadoop fs -ls hdfs://localhost:8020/
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8081 /mvn-web/hadoop-web-app.jar
hadoop fs -cat /tmp/logs/hadoop/logs/application_1488213410571_0001/hadoop-yarn_50011
kill -9 $(cat /app-info/run.pid)
```

**Sources:**

https://github.com/vangj/vagrant-hadoop-2.3.0

https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html

https://dzone.com/articles/effective-testing-strategies

http://stayrelevant.globant.com/debug-your-mapreduce-program-locally/

https://pravinchavan.wordpress.com/2013/04/05/remote-debugging-of-hadoop-job-with-eclipse/

https://github.com/tequalsme/hadoop-examples

http://protocolsofmatrix.blogspot.ru/2016/09/install-hadoop-273-on-ubuntu-1604-lts.html

http://docs.spring.io/autorepo/docs/spring-hadoop/1.0.0.RC1/reference/htmlsingle/

https://understanding-hadoop-by-mahesh.blogspot.ru/2017/01/hadoop-273-multi-node-cluster-setup-in.html

http://stackoverflow.com/questions/21345022/hadoop-is-not-showing-my-job-in-the-job-tracker-even-though-it-is-running