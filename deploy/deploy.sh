#!/bin/bash
set -e

cd /home/ubuntu/deploy

docker compose pull
docker compose up -d
docker ps