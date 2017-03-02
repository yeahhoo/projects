#!/bin/bash

export HADOOP_PREFIX=/usr/local/hadoop
HADOOP_VERSION=hadoop-2.7.3
HADOOP_ARCHIVE=${HADOOP_VERSION}.tar.gz
JAVA_ARCHIVE=jdk-8u121-linux-x64.tar.gz
JDK_VERSION=jdk1.8.0_121
HADOOP_MIRROR_DOWNLOAD=http://apache-mirror.rbc.ru/pub/apache/hadoop/common/hadoop-2.7.3/hadoop-2.7.3.tar.gz
JDK_DOWNLOAD_LINK=http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jdk-8u121-linux-x64.tar.gz

function fileExists {
	FILE=/vagrant/resources/$1
	if [ -e $FILE ]
	then
		return 0
	else
		return 1
	fi
}

function installUtils {
    # installing tofordos
	sudo apt-get install tofrodos
	sudo ln -s /usr/bin/fromdos /usr/bin/dos2unix

    # installing curl
	sudo apt-get install curl -y

    # installing equip
    wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_base.sh && bash equip_base.sh
    rm -f equip_base.sh
}

function convertFilesUnixFormat {
    /usr/bin/dos2unix /vagrant/resources/run-spring-app.sh
    /usr/bin/dos2unix /vagrant/resources/create-hadoop-user.sh
    /usr/bin/dos2unix /vagrant/resources/setup-run-hadoop.sh
    /usr/bin/dos2unix /vagrant/resources/create-private-keys.sh
}

function installJava {
    sudo mkdir -p /usr/local
    sudo mkdir -p /usr/lib/jvm
	if fileExists $JAVA_ARCHIVE; then
		installLocalJava
	else
		installRemoteJava
	fi
}

function installLocalJava {
	echo "install Java from local file"
	FILE=/vagrant/resources/$JAVA_ARCHIVE
	tar -xvf $FILE -C /usr/local
	setUpJava
}

function installRemoteJava {
	echo "Install Remote Java"
    curl -L --cookie "oraclelicense=accept-securebackup-cookie" ${JDK_DOWNLOAD_LINK} -o ${JAVA_ARCHIVE}
	tar -xvf ${JAVA_ARCHIVE} -C /usr/local
	rm jdk-8u121-linux-x64.tar.gz
	setUpJava
	echo "current path"
	pwd
}

function setUpJava {
    sudo mv /usr/local/jdk1.8.* /usr/lib/jvm/
	sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/${JDK_VERSION}/bin/java" 1
	sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/${JDK_VERSION}/bin/javac" 1
	sudo update-alternatives --install "/usr/bin/javaws" "javaws" "/usr/lib/jvm/${JDK_VERSION}/bin/javaws" 1

	sudo chmod a+x /usr/bin/java
	sudo chmod a+x /usr/bin/javac
	sudo chmod a+x /usr/bin/javaws
	sudo chown -R root:root /usr/lib/jvm/${JDK_VERSION}

	export JAVA_HOME=/usr/lib/jvm/${JDK_VERSION}
	export JAVA=/usr/lib/jvm/${JDK_VERSION}
	export PATH=$PATH:$JAVA_HOME/bin

	# https://risenfall.wordpress.com/2011/06/28/howto-start-apache-hadoop-in-debug-mode/
	# https://github.com/vangj/vagrant-hadoop-2.3.0/blob/master/setup.sh
	ln -s /usr/lib/jvm/${JDK_VERSION} /usr/local/java
	echo export JAVA_HOME=${JAVA_HOME} >> /etc/profile.d/java.sh
	echo export JAVA=${JAVA_HOME} >> /etc/profile.d/java.sh
	echo export PATH=${PATH} >> /etc/profile.d/myenv.sh
}

function installLocalHadoop {
	echo "install hadoop from local file"
	FILE=/vagrant/resources/$HADOOP_ARCHIVE
	tar -xzf $FILE -C /usr/local
}

function installRemoteHadoop {
	echo "install hadoop from remote file"
	curl -o /home/vagrant/${HADOOP_ARCHIVE} -O -L $HADOOP_MIRROR_DOWNLOAD
	tar -xzf /home/vagrant/${HADOOP_ARCHIVE} -C /usr/local
}

function installHadoop {
	if fileExists $HADOOP_ARCHIVE; then
		installLocalHadoop
	else
		installRemoteHadoop
	fi
}

function setupAndRunHadoop {
    ln -s /usr/local/${HADOOP_VERSION} /usr/local/hadoop

	echo "copying over hadoop configuration files"
	cp -f /vagrant/resources/core-site.xml /usr/local/hadoop/etc/hadoop
	cp -f /vagrant/resources/hdfs-site.xml /usr/local/hadoop/etc/hadoop
	cp -f /vagrant/resources/mapred-site.xml /usr/local/hadoop/etc/hadoop
	cp -f /vagrant/resources/yarn-site.xml /usr/local/hadoop/etc/hadoop
	cp -f /vagrant/resources/slaves /usr/local/hadoop/etc/hadoop
	cp -f /vagrant/resources/hadoop-env.sh /usr/local/hadoop/etc/hadoop
	cp -f /vagrant/resources/yarn-env.sh /usr/local/hadoop/etc/hadoop
	cp -f /vagrant/resources/yarn-daemon.sh /usr/local/hadoop/sbin
	cp -f /vagrant/resources/mr-jobhistory-daemon.sh /usr/local/hadoop/sbin
	cp -f /vagrant/resources/hadoop /usr/local/hadoop/bin

    echo "setting up hadoop service"
    sudo cp -f /vagrant/resources/hadoop.sh /etc/profile.d/hadoop.sh
    sudo rm /etc/profile.d/myenv.sh
    echo export PATH=${PATH}:${HADOOP_PREFIX}/bin >> /etc/profile.d/myenv.sh

    sudo cp -f /vagrant/resources/hadoop-service /etc/init.d/hadoop-service
    chmod 777 /etc/init.d/hadoop-service

	#echo "configuring environment variables"
	#export HADOOP=/usr/local/hadoop
	#printf "\nexport PATH=${PATH}:${HADOOP}/bin\n" >> /home/vagrant/.bashrc
	#printf "\nexport PATH=${PATH}:${HADOOP}/bin\n" >> /home/hadoop/.bashrc
	#source ~/.bashrc

    echo "creating hadoop user"
    /vagrant/resources/create-hadoop-user.sh

    echo "modifying rights to hadoop logs"
    sudo mkdir /usr/local/hadoop/logs
    sudo chown hadoop:hadoop /usr/local/hadoop/logs

    sudo mkdir /app-info
    sudo chmod 777 /app-info
	echo "creating hadoop directories, generating keys and run hadoop on behalf of hadoop"
    su -c "sh /vagrant/resources/setup-run-hadoop.sh" hadoop
}

installUtils
convertFilesUnixFormat
installJava
installHadoop
setupAndRunHadoop