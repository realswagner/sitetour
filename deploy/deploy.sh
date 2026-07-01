#!/bin/bash
set -e

cd /home/ubuntu/deploy

sudo docker compose pull
sudo docker compose up -d
sudo docker ps