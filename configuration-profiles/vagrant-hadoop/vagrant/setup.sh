#!/bin/bash
export HADOOP_PREFIX=/usr/local/hadoop
HADOOP_ARCHIVE=hadoop-2.7.3.tar.gz
JAVA_ARCHIVE=jdk-7u51-linux-x64.gz
HADOOP_MIRROR_DOWNLOAD=http://apache-mirror.rbc.ru/pub/apache/hadoop/common/hadoop-2.7.3/hadoop-2.7.3.tar.gz 
	
function fileExists {
	FILE=/vagrant/resources/$1
	if [ -e $FILE ]
	then
		return 0
	else
		return 1
	fi
}

function installRemoteJava {
	echo "Install Java"

	wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_base.sh && bash equip_base.sh

	sudo apt-get install curl -y 
	curl -L --cookie "oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jdk-8u121-linux-x64.tar.gz -o jdk-8u121-linux-x64.tar.gz
	tar -xvf jdk-8u121-linux-x64.tar.gz

	sudo mkdir -p /usr/lib/jvm
	sudo mv ./jdk1.8.* /usr/lib/jvm/

	sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk1.8.0_121/bin/java" 1
	sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk1.8.0_121/bin/javac" 1
	sudo update-alternatives --install "/usr/bin/javaws" "javaws" "/usr/lib/jvm/jdk1.8.0_121/bin/javaws" 1

	sudo chmod a+x /usr/bin/java 
	sudo chmod a+x /usr/bin/javac 
	sudo chmod a+x /usr/bin/javaws
	sudo chown -R root:root /usr/lib/jvm/jdk1.8.0_121

	rm jdk-8u121-linux-x64.tar.gz
	rm -f equip_base.sh 

	# java -version
	# hdfs dfs -put test.txt /
	# hadoop jar ./java-sync-1.0-SNAPSHOT.jar
	export JAVA_HOME=/usr/lib/jvm/jdk1.8.0_121
	export JAVA=/usr/lib/jvm/jdk1.8.0_121
	export PATH=$PATH:$JAVA_HOME/bin
	#printf "\nexport JAVA=/usr/lib/jvm/jdk1.8.0_121\n" >> /home/vagrant/.bashrc
	#printf "\nexport JAVA_HOME=/usr/lib/jvm/jdk1.8.0_121\n" >> /home/vagrant/.bashrc
	#source ~/.bashrc
	
	# https://risenfall.wordpress.com/2011/06/28/howto-start-apache-hadoop-in-debug-mode/
	# https://github.com/vangj/vagrant-hadoop-2.3.0/blob/master/setup.sh
	ln -s /usr/lib/jvm/jdk1.8.0_121 /usr/local/java
	echo export JAVA_HOME=${JAVA_HOME} >> /etc/profile.d/java.sh
	echo export JAVA=${JAVA_HOME} >> /etc/profile.d/java.sh
	echo export PATH=${PATH} >> /etc/profile.d/java.sh	
	
}

function installLocalHadoop {
	echo "install hadoop from local file"
	FILE=/vagrant/resources/$HADOOP_ARCHIVE
	tar -xzf $FILE -C /usr/local
}

function installRemoteHadoop {
	echo "install hadoop from remote file"
	curl -o /home/vagrant/hadoop-2.7.3.tar.gz -O -L $HADOOP_MIRROR_DOWNLOAD
	tar -xzf /home/vagrant/hadoop-2.7.3.tar.gz -C /usr/local
}

function installHadoop {
	if fileExists $HADOOP_ARCHIVE; then
		installLocalHadoop
	else
		installRemoteHadoop
	fi
}

function setupHadoop {
	echo "creating hadoop directories"
	mkdir /tmp/hadoop-namenode
	mkdir /tmp/hadoop-logs
	mkdir /tmp/hadoop-datanode
	ln -s /usr/local/hadoop-2.7.3 /usr/local/hadoop
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
	echo "modifying permissions on local file system"
	chown -fR vagrant /tmp/hadoop-namenode
    chown -fR vagrant /tmp/hadoop-logs
    chown -fR vagrant /tmp/hadoop-datanode
	mkdir /usr/local/hadoop-2.7.3/logs
	chown -fR vagrant /usr/local/hadoop-2.7.3/logs
}

function startHadoopService {
	echo "creating hadoop environment variables"
	cp -f /vagrant/resources/hadoop.sh /etc/profile.d/hadoop.sh
	
	#echo "setting up namenode"
	export HADOOP=/usr/local/hadoop
	printf "\nexport PATH=${PATH}:${HADOOP}/bin\n" >> /home/vagrant/.bashrc
	source ~/.bashrc
	/usr/local/hadoop-2.7.3/bin/hdfs namenode -format myhadoop

	echo "setting up hadoop service"
	cp -f /vagrant/resources/hadoop /etc/init.d/hadoop
	chmod 777 /etc/init.d/hadoop

	echo "starting hadoop service"
	#/usr/local/hadoop-2.7.3/sbin/start-dfs.sh
	#/usr/local/hadoop-2.7.3/sbin/start-yarn
	service hadoop start	### todo change it to 2.7.3 version
}


function initHdfsTempDir {
	$HADOOP_PREFIX/bin/hdfs --config $HADOOP_PREFIX/etc/hadoop dfs -mkdir /tmp
	$HADOOP_PREFIX/bin/hdfs --config $HADOOP_PREFIX/etc/hadoop dfs -chmod -R 777 /tmp
}

installRemoteJava
installHadoop
setupHadoop
startHadoopService
initHdfsTempDir