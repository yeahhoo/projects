Training project for implementing OATH2 server (auth and REST) and client that uses it.

Project set-up:
just specify correct client and server URLs for the file: client.yml (client module)

To run the project execute in command line:
	
for server: 
	mvn clean install -DskipTests=true spring-boot:run
	
for client: 
	1) mvn clean install
	2) cd oauth2-client-web
	3) mvn spring-boot:run


After the projects started hit the URL and try to authorize: http://localhost:8001/client/

credentials: me/me, dba/dba, user/user, admin/admin

