**Docker Spark config**

This instruction creates Docker with installed Spark cluster which gets tasks from Spring Boot application. HDFS is used as FS.

```sh
cd /docker-spark
mvn clean install
cd /docker-spark/docker
docker network create hadoop
docker-compose up -d
# add data to hdfs
docker exec web-app /scripts/add-data-unix.sh
```

The script gives you opportunity to run Spark task whether through spark-submit script or as web - pick one you need.

**Spark-submit script:**

```sh
# connect to the container
docker exec -it web-app /bin/bash
# please note that it might not work due to lack of resources - Spring app has taken it already. So stop or disable Spring Spark context if you still want to run it.
/usr/spark-2.1.0/bin/spark-submit --master spark://spark-master:7077 --conf spark.ui.port=10981 /opt/jar/target/spark-jar-module.jar
# if it didn't work out try to submit it on another machine
docker exec -it spark-master /bin/bash
/usr/spark-2.1.0/bin/spark-submit  /opt/jar/target/spark-jar-module.jar
# check that output folder created and filled with content
HTTP GET http://192.168.99.100:50070/explorer.html
```

**Web application mode:**

```sh
# check if application is up and running and then submit job
HTTP GET check: http://192.168.99.100:11091/server/test
HTTP GET job run: http://192.168.99.100:11091/server/runJob?input=test2.txt&output=output1
check spark console: http://192.168.99.100:8080/
```

**Mounting Troubleshooting :**

The solution described in ../docker-mongo/README.md. For this project is needed to create shares hadoop-data, spark-conf, spark-module-jar and mount them, e.g.

```sh
sudo mount -t vboxsf hadoop-data /d/projects/configuration-profiles/docker-spark/docker/hadoop-data
sudo mount -t vboxsf spark-conf /d/projects/configuration-profiles/docker-spark/docker/conf
sudo mount -t vboxsf spark-module-web /d/projects/configuration-profiles/docker-spark/spark-module-web
sudo mount -t vboxsf spark-module-jar /d/projects/configuration-profiles/docker-spark/spark-module-jar
```

**Useful Commands:**

```sh
docker exec -it web-app /bin/bash
docker stop namenode datanode1 datanode2 spark-master spark-worker web-app
docker rm namenode datanode1 datanode2 spark-master spark-worker web-app
/usr/spark-2.1.0/bin/spark-submit --class org.apache.spark.examples.SparkPi --master spark://spark-master:7077 --conf spark.ui.port=10981 /usr/spark-2.1.0/examples/jars/spark-examples_2.11-2.1.0.jar 10
spark-shell  --master spark://spark-master:7077 --conf spark.ui.port=10981
hdfs dfs -rm -r  hdfs://namenode:8020/output
```

**Sources:**

https://github.com/gettyimages/docker-spark

https://github.com/vkorukanti/spark-docker-compose

https://github.com/big-data-europe/docker-hadoop-spark-workbench

https://databricks.gitbooks.io/databricks-spark-knowledge-base/content/troubleshooting/connectivity_issues.html

https://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/launcher/package-summary.html

https://github.com/SpringOne2GX-2014/SparkForSpring

http://bencane.com/2014/09/02/understanding-exit-codes-and-how-to-use-them-in-bash-scripts/