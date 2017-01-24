FROM ubuntu:14.04
MAINTAINER me2 <me@me.com>

# Installation:
# Import MongoDB public GPG key AND create a MongoDB list file
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
RUN echo "deb http://repo.mongodb.org/apt/ubuntu $(cat /etc/lsb-release | grep DISTRIB_CODENAME | cut -d= -f2)/mongodb-org/3.2 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.2.list

# Update apt-get sources AND install MongoDB
RUN apt-get update && apt-get install -y mongodb-org

# installing vim
RUN ["apt-get", "update"]
RUN ["apt-get", "install", "-y", "vim"]

RUN ["apt-get", "install", "tofrodos"]
RUN ["ln", "-s", "/usr/bin/fromdos", "/usr/bin/dos2unix"]

# Create the MongoDB data directory
RUN mkdir -p /data/db
RUN mkdir -p /data/logs
RUN mkdir -p /mongo-confs

# Expose port 27017 from the container to the host to let user connect to it via -p 30001:27017
EXPOSE 27017
# attaching data folder to the host FS -v /host/path:/data/db
VOLUME /data/db
VOLUME /data/logs

ADD run.sh /run.sh
RUN ["chmod", "+x", "/run.sh"]
# UNIX recoding of CLRF symbols
RUN ["/usr/bin/dos2unix", "/run.sh"]
COPY init-replica-script.js /mongo-confs
# by default replica initialization flag is no - it must be active only for last node
ENV IS_REPL_INIT no

# Set usr/bin/mongod as the dockerized entry-point application
#ENTRYPOINT /usr/local/bin/run.sh
ENTRYPOINT ["/run.sh"]