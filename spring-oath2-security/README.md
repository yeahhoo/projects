Training project for implementing OATH2 server (auth and REST) and client that uses it.

Project set-up:
just specify correct client and server URLs in the file: client.yml (client module)

To run the project execute in command line (both server and client): mvn clean install

After the projects started hit the URL and try to authorize: http://localhost:8001/client/

credentials: me/me, dba/dba, user/user, admin/admin

DOCKER USE:
1) Have Docker installed  
2) Don't forget to forward ports 8001, 9001, 9003 on your Virtual Machine 
3) navigate to the project folder (web-module: oauth2-server/oauth2-server-web) with Docker Bash Terminal;
4) Create image: mvn clean package docker:build
5) run container for server: docker run -it -p 9001:9001 -p 9003:9003 oauth2-server-web

if you want to run both server and client under docker then:
1) rebuild image with the profile: mvn clean package docker:build
2) start server container: docker run -d -p 9001:9001 -p 9003:9003 --name secserver --net=secnetwork oauth2-server-web
3) cd ../../oauth2-client/oauth2-client-web
4) Create client container: mvn clean package -Pdocker docker:build
5) start the client container: docker run -d -p 8001:8001 --name secclient --net=secnetwork oauth2-client-web

to stop the containers:
1) stop server container: docker stop secserver
2) stop client container: docker stop secclient

optionally remove the containers:
docker rm -f secserver
docker rm -f secclient


useful commands:
connect to virtual machine: docker-machine ssh default
list containers: docker ps


