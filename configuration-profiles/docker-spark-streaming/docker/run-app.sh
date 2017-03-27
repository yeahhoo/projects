#!/bin/bash

cmd="java -Xmx1024m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=11092 -jar /opt/web/target/spark-streaming-web-app.jar"

exec ${cmd}

