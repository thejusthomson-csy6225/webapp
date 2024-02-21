#!/usr/bin/bash

sudo cp /tmp/webapp-launch.service /etc/systemd/system/webapp-launch.service
sudo systemctl daemon-reload
sudo systemctl enable webapp-launch.service