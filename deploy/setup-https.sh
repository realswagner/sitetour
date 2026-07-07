#!/bin/bash
set -e

ENV_FILE="/home/ubuntu/deploy/.env"

if [ -f "$ENV_FILE" ]; then
  set -a
  source "$ENV_FILE"
  set +a
fi

DOMAIN_NAME="${DOMAIN_NAME:?DOMAIN_NAME is required}"
SUBDOMAIN_NAME="${APP_DOMAIN:?APP_DOMAIN is required}"
EMAIL="${LETSENCRYPT_EMAIL:?LETSENCRYPT_EMAIL is required}"

CERT_PATH="/etc/letsencrypt/live/${DOMAIN_NAME}/fullchain.pem"

mkdir -p /var/www/certbot

if [ ! -f "$CERT_PATH" ]; then
  echo "No certificate found. Requesting Let's Encrypt certificate..."

  certbot certonly \
    --webroot \
    -w /var/www/certbot \
    -d "$DOMAIN_NAME" \
    -d "$SUBDOMAIN_NAME" \
    --email "$EMAIL" \
    --agree-tos \
    --non-interactive
else
  echo "Certificate already exists. Skipping initial request."
fi

echo "HTTPS setup complete."