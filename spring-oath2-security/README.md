Training project for implementing OATH2 server (auth and REST) and client that uses it.

To run the project execute in command line (both client and server): mvn clean install -DskipTests=true spring-boot:run

After the projects started hit the URL and try to authorize: http://localhost:8001/client/

credentials: me/me, dba/dba, user/user, admin/admin

