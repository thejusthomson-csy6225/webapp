#!/usr/bin/bash

#install mySQL
sudo dnf install mysql-server -y

#install java
sudo dnf install java-17-openjdk-devel.x86_64 -y

#enable mysql
sudo systemctl enable mysqld

#start service
sudo systemctl start mysqld

#change password
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';"