#!/bin/bash

set -e

echo "===== updating Ubuntu ====="

apt-get update
apt-get upgrade -y

echo "===== installing utilities ====="

apt-get install -y \
    git \
    curl \
    unzip \
    ca-certificates \
    gnupg \
    lsb-release \
    certbot

echo "===== installing AWS CLI ====="

curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "/tmp/awscliv2.zip"

cd /tmp
unzip -q awscliv2.zip

/tmp/aws/install

aws --version

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

usermod -aG docker ubuntu

mkdir -p /home/ubuntu/deploy

chown -R ubuntu:ubuntu /home/ubuntu/deploy

echo "===== configuring PostgreSQL EBS volume ====="

POSTGRES_MOUNT="/mnt/sitetour-postgres"
POSTGRES_DATA="$POSTGRES_MOUNT/data"

# Wait for attached EBS volume to appear
for i in {1..30}; do
    if [ -b /dev/nvme1n1 ]; then
        echo "PostgreSQL EBS volume found at /dev/nvme1n1"
        break
    fi

    echo "Waiting for PostgreSQL EBS volume..."
    sleep 5
done

if [ ! -b /dev/nvme1n1 ]; then
    echo "PostgreSQL EBS volume not found. Skipping mount setup."
else
    # Format only if blank
    if sudo file -s /dev/nvme1n1 | grep -q "data"; then
        echo "Formatting PostgreSQL EBS volume..."
        mkfs.ext4 /dev/nvme1n1
    else
        echo "PostgreSQL EBS volume already has a filesystem. Skipping format."
    fi

    mkdir -p "$POSTGRES_MOUNT"

    UUID=$(blkid -s UUID -o value /dev/nvme1n1)

    if ! grep -q "$UUID" /etc/fstab; then
        echo "UUID=$UUID $POSTGRES_MOUNT ext4 defaults,nofail 0 2" >> /etc/fstab
    fi

    mount -a

    mkdir -p "$POSTGRES_DATA"
    chown -R 999:999 "$POSTGRES_DATA"
    chmod 700 "$POSTGRES_DATA"

    echo "PostgreSQL EBS volume mounted at $POSTGRES_MOUNT"
fi

echo "===== Bootstrap Complete ====="