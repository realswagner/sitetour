#!/bin/bash
set -e

cd /home/ubuntu/deploy

sudo docker compose pull
sudo docker compose up -d

echo "Waiting for application health check..."

for i in {1..30}; do
  if sudo docker exec sitetour-app wget -qO- http://localhost:8080/login > /dev/null; then
    echo "Health check passed."

    echo "Recreating Nginx to refresh Docker DNS..."
    sudo docker compose up -d --force-recreate nginx

    sudo docker ps
    exit 0
  fi

  echo "Health check attempt $i failed. Retrying..."
  sleep 5
done

echo "Health check failed."
sudo docker compose logs --tail=100 app
exit 1