#!/usr/bin/bash

sudo chown csye6225:csye6225 /tmp/webapp-launch.service
sudo cp /tmp/webapp-launch.service /etc/systemd/system/webapp-launch.service
sudo systemctl daemon-reload
sudo systemctl enable webapp-launch.service