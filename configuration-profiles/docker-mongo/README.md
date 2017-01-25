**Docker Mongo Replica config**

This instruction creates docker with installed MongoDb ReplicaSet. Follow the below steps to have it:

1) Build image from docker console
```sh
cd configuration-profiles/docker-mongo
mvn clean install docker:build
```

2) Add Network
```sh
docker network create my-mongo-cluster
```

3) Add Secondary Nodes:
```sh
docker run -d -p 30002:27017 --name mongo2 --net my-mongo-cluster docker-mongo
docker run -d -p 30003:27017 --name mongo3 --net my-mongo-cluster docker-mongo
```

4) Add Primary Node and initialize replica:
```sh
docker run -d -p 30001:27017 -e IS_REPL_INIT='yes' -v /D/mongo-data:/data/db --privileged --name mongo1 --net my-mongo-cluster docker-mongo
```

5) If you have mongo installed on your local machine then you can connect with the following way:
```sh
## know docker IP
docker-machine ls
## connect via local mongo
mongo --host 192.168.99.100 --port 30001
```


**Useful Commands:**
```sh
docker ps -a --no-trunc # lists containers with full info
docker run -i -e IS_REPL_INIT='yes' docker-mongo # good for debug, -e means environmental variable
docker exec -it <container-id> /bin/bash # connects to a container-id
docker-machine ls # lists all VMs
docker-machine ip # gives VM IP
docker stop $(docker ps -a | grep docker-mongo | awk '{print $1}') # stops all docker-mongo containers
docker rm mongo1 mongo2 mongo3 # removing all mongo containers
mongo --host 192.168.99.100 --port 30001 # mongo to connect to a remote server
```

**Mounting Troubleshooting :**

TBD


**Sources:**

https://github.com/docker-library/mongo/blob/c9a1b066a0f35f679c2f8e1854a21e025867d938/3.0/Dockerfile

https://github.com/popestepheng/mongo-replica-set

http://www.sohamkamani.com/blog/2016/06/30/docker-mongo-replica-set/

https://hub.docker.com/_/mongo/

http://www.carlboettiger.info/2014/08/29/docker-notes.html

https://docs.docker.com/v1.10/engine/userguide/containers/dockervolumes/

http://serverfault.com/questions/674974/how-to-mount-a-virtualbox-shared-folder
