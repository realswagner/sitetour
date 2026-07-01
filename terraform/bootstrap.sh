#!/bin/bash

set -e

echo "===== udpating Ubuntu ====="

apt-get update
apt-get upgrade -y

echo "===== installing utilities ====="

apt-get install -y \
    git \
    curl \
    unzip \
    ca-certificates \
    gnupg \
    lsb-release

echo "===== installing Docker ====="

install -m 0755 -d /etc/apt/keyrings

curl -fsSL https://download.docker.com/linux/ubuntu/gpg \
| gpg --dearmor -o /etc/apt/keyrings/docker.gpg

chmod a+r /etc/apt/keyrings/docker.gpg

echo \
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" \
| tee /etc/apt/sources.list.d/docker.list > /dev/null

apt-get update

apt-get install -y \
docker-ce \
docker-ce-cli \
containerd.io \
docker-buildx-plugin \
docker-compose-plugin

systemctl enable docker
systemctl start docker

echo "===== Bootstrap Complete ====="