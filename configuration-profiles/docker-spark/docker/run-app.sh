#!/bin/bash

# it works but it's nasty
# /scripts/add-data-unix.sh

cmd="java -Xmx1024m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=11092 -jar /opt/web/target/spark-web-app.jar"

exec ${cmd}

