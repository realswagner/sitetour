#!/bin/bash
set -e

DOMAIN_NAME="realswagner.dev"
SUBDOMAIN_NAME="sitetour.realswagner.dev"
EMAIL="stephenwagner94@gmail.com"

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