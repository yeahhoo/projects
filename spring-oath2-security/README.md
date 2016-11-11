Training project for implementing OATH2 server (auth and REST) and client that uses it.

Project set-up:
just specify correct client and server URLs for the file: client.yml (client module)

To run the project execute in command line:
	
for server: mvn clean install spring-boot:run
for client: mvn clean install


After the projects started hit the URL and try to authorize: http://localhost:8001/client/

credentials: me/me, dba/dba, user/user, admin/admin

