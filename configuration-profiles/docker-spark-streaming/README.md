**Docker Spark config**

This instruction creates Docker with installed Spark cluster which processes streaming received over network.

```sh
cd /docker-spark-streaming
mvn clean install
cd /docker-spark-streaming/docker
docker network create spark
docker-compose up -d
```

Here are the steps to follow to get set-up streaming

**Spark-streaming by Web-App:**

```sh
# check if application is up and running
HTTP GET check: http://192.168.99.100:11091/server/test
# run spark streaming
docker exec -it web-app /usr/spark-2.1.0/bin/spark-submit \
--jars /opt/mongo-spark-connector_2.11-2.0.0.jar,/opt/mongo-java-driver-3.4.2.jar \
/opt/jar/target/spark-streaming-jar-module.jar

# connect to mongo
mongo --host 192.168.99.100 --port 30001
# check received data
db.spark.find().sort({"list": 1})
# periodically clean it
db.spark.remove({})
```

You can also do it using netcat

```sh
# start netcat server
docker exec -it web-app nc -l -p 9999
# open second docker terminal and submit Spark job:
docker exec -it web-app /usr/spark-2.1.0/bin/spark-submit \
--jars /opt/mongo-spark-connector_2.11-2.0.0.jar,/opt/mongo-java-driver-3.4.2.jar \
/opt/jar/target/spark-streaming-jar-module.jar
```


**Mounting Troubleshooting :**

The solution described in ../docker-mongo/README.md. For this project is needed to create shares spark-streaming-module-jar, spark-streaming-module-web and mount them, e.g.

```sh
sudo mount -t vboxsf spark-streaming-module-jar /d/projects/configuration-profiles/docker-spark-streaming/spark-streaming-module-jar
sudo mount -t vboxsf spark-streaming-module-web /d/projects/configuration-profiles/docker-spark-streaming/spark-streaming-module-web
```

**Useful Commands:**

```sh
docker exec -it web-app /bin/bash
docker stop spark-master spark-worker spark-worker2 web-app mongo
docker rm spark-master spark-worker spark-worker2 web-app mongo
/usr/spark-2.1.0/bin/spark-submit  /opt/jar/target/spark-streaming-jar-module.jar
```

**Sources:**

https://github.com/gettyimages/docker-spark

http://spark.apache.org/docs/latest/streaming-programming-guide.html