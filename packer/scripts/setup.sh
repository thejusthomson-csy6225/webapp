#!/usr/bin/bash

#install mySQL
sudo dnf install mysql-server -y

#install java
sudo dnf install java-17-openjdk-devel.x86_64 -y

#enable mysql
sudo systemctl enable mysqld

sleep 5
#start service
sudo systemctl start mysqld

sleep 5
#change password
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';"