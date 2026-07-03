#!/bin/bash
set -e

cd /home/ubuntu/deploy

sudo docker compose pull
sudo docker compose up -d
# recreate nginx to avoid ip issues 
echo "Recreating Nginx to refresh Docker DNS..."
docker compose up -d --force-recreate nginx
#health check
echo "Waiting for application health check..."

for i in {1..30}; do
  if curl -fsS http://localhost:8080/login > /dev/null; then
    echo "Health check passed."
    sudo docker ps
    exit 0
  fi

  echo "Health check attempt $i failed. Retrying..."
  sleep 5
done

echo "Health check failed."
sudo docker compose logs --tail=100 app
exit 1