#!/usr/bin/bash

#install mySQL
sudo dnf install mysql-server -y

#start service
sudo systemctl start mysqld

#install java 
sudo dnf install java-17-openjdk-devel.x86_64 -y

#change password
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'MyCentosPass8#';"