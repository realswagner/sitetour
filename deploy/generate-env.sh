#!/usr/bin/env bash
set -euo pipefail

PARAMETER_PATH="/sitetour/dev"
ENV_FILE="/home/ubuntu/deploy/.env"
AWS_REGION="ap-northeast-1"

echo "Generating .env from AWS SSM Parameter Store..."

aws ssm get-parameters-by-path \
  --path "$PARAMETER_PATH" \
  --with-decryption \
  --region "$AWS_REGION" \
  --query "Parameters[*].[Name,Value]" \
  --output text \
| while read -r name value; do
    key="$(basename "$name")"
    echo "${key}=${value}"
  done > "$ENV_FILE"

chmod 600 "$ENV_FILE"

echo ".env generated successfully at $ENV_FILE"