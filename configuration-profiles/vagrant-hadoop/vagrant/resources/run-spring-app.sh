
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8081 /mvn-web/hadoop-web-app.jar &
