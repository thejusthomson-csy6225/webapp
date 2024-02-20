#!/usr/bin/bash

#install unzip
sudo dnf install unzip -y

#install wget
sudo dnf install wget -y

#install java 
sudo dnf install java-17-openjdk-devel.x86_64 -y

#install mySQL
sudo dnf install mysql-server -y

#start service
sudo systemctl start mysqld

#install maven
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz -P /tmp

#install maven
sudo tar xf /tmp/apache-maven-3.9.6-bin.tar.gz -C /opt

# #export
# #export JAVA_HOME=/usr/lib/jvm/java-openjdk
# #export PATH=$JAVA_HOME/bin:$PATH
# export M2_HOME=/opt/apache-maven-3.9.6
# export MAVEN_HOME=/opt/apache-maven-3.9.6
# export PATH=${M2_HOME}/bin:${PATH}

# #install mySQL
# sudo dnf install mysql-server

# #start service
# sudo systemctl start mysqld

# cd /tmp/webapp || exit

# #./mvnw.sh spring-boot:run

# #test and run spring boot app
# mvn test

# sleep 10

# mvn spring-boot:run