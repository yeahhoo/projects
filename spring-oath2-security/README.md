Training project for implementing OATH2 server (auth and REST) and client that uses it.

Project set-up:
just specify correct client and server URLs in the file: client.yml (client module)

To run the project execute in command line (both server and client): 
```sh
mvn clean install
```
After the projects start hit the URL and try to authorize: http://localhost:8001/client/

credentials: me/me, dba/dba, user/user, admin/admin

**INTEGRATION TESTS:**

To run integration tests do the following:

1) Have oauth2-server launched;

2) Run client build with param: it.skip=false
```sh
mvn clean install -Dit.skip=false
```

**DOCKER USAGE:**

1) Have Docker installed

2) Don't forget to forward ports 8001, 9001, 9003, 9005 on your Virtual Machine

3) navigate to the project folder (web-module: oauth2-server/oauth2-server-web) with Docker Bash Terminal;

4) Create image: 
```sh
cd oauth2-server/oauth2-server-web
mvn clean package docker:build
```
5) run container for server: docker run -it -p 9001:9001 -p 9003:9003 -p 9005:9005 oauth2-server-web

if you want to run both server and client under docker then:

1) rebuild server image with: 
```sh
mvn clean package docker:build
```
2) start server container:
```sh
docker run -d -p 9001:9001 -p 9003:9003 --name secserver --net=secnetwork oauth2-server-web
```
3) cd to the client-web module
```sh
cd ../../oauth2-client/oauth2-client-web
```
4) Create client container and start it:
```sh
mvn clean package -Pdocker docker:build
docker run -d -p 8001:8001 --name secclient --net=secnetwork oauth2-client-web
```

to stop the containers:

1) stop server container: docker stop secserver

2) stop client container: docker stop secclient

optionally remove the containers:
```sh
docker rm -f secserver
docker rm -f secclient
```


**NODE USAGE:**

After building the application Node.js installs as local utility with possibility to run it. 
```sh
cd oauth2-client/oauth2-client-web/node
$ node
# inside the node.js console run the following script:

var grunt = require('grunt');

grunt.initConfig({ 
        webpack: { 
            someTarget: require("../jsconfs/webpack.config.js") 
        } 
   });
   
grunt.tasks("default");
```

**USEFUL COMMANDS:**
```sh
connect to virtual machine: docker-machine ssh default
list containers: docker ps
```


