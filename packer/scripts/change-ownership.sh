#!/usr/bin/bash

# Set ownership of the copied JAR file
sudo chown csye6225:csye6225 /tmp/webapp-0.0.1-SNAPSHOT.jar

sudo mv /tmp/webapp-0.0.1-SNAPSHOT.jar /opt/webapp-0.0.1-SNAPSHOT.jar