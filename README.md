# SiteTour Interview Management System
Project Status

✅ Completed

✅ Live Demo Available

✅ Automated CI/CD

✅ Infrastructure as Code

✅ Persistent PostgreSQL Database

# Overview

Java・Spring Boot・AWSを用いて開発した面接管理システムです。
Spring BootによるWebアプリケーション開発だけでなく、
Terraform・Docker・GitHub Actions・AWSを利用したインフラ構築およびCI/CDまでを一貫して実装しました。

SiteTour is an interview management system built with Spring Boot and AWS.
In addition to developing the web application itself, this project demonstrates infrastructure automation,
CI/CD, secure configuration management, and cloud deployment using Terraform, Docker, GitHub Actions, and AWS.

# Live Demo
https://sitetour.realswagner.dev
Note: This application is deployed on a personal AWS account for demonstration purposes. 
The live demo may occasionally be unavailable due to maintenance, cost optimization, or infrastructure updates.

注：本アプリケーションは、デモンストレーションを目的として個人のAWSアカウント上にデプロイされています。
メンテナンス、コスト最適化、またはインフラストラクチャの更新などの理由により、ライブデモが一時的に利用できない場合があります。

<img width="886" height="424" alt="image" src="https://github.com/user-attachments/assets/8bb8655b-783d-4a41-8ff7-c0bd486efab0" />

# Technology Stack

Backend

• Java 21
• Spring Boot
• Spring Security
• Spring Data JPA
• Hibernate

Frontend

• Thymeleaf
• HTML
• CSS
• JavaScript

Database

• PostgreSQL

Infrastructure

• AWS EC2
• Amazon EBS
• AWS Systems Manager
• AWS Parameter Store
• IAM

DevOps

• Docker
• Docker Compose
• GitHub Actions
• GitHub Container Registry
• Terraform
• Nginx

# Features

主な機能

・社員管理 • Employee Management

・チーム管理 • Team Management

・ユーザー管理 • User Management

・面接スケジュール管理 • Interview Scheduling

・パーソナルレポート管理 • Interview Reports

・ダッシュボード • Dashboard

・ロールベース認証 • Role-based Authentication & Authorization

・監査ログ • Audit Logging

・CSV出力 • CSV Export

・Excel出力 • Excel Export

・HTTPS対応 • HTTPS

・AWSへの自動デプロイ • Automated AWS Deployment

# Architecture
<img width="972" height="767" alt="image" src="https://github.com/user-attachments/assets/664bb137-be08-4396-808d-780bba398d83" />

## Quick Deployment

1. Configure AWS credentials locally. ローカル環境でAWS認証情報を設定します。
2. Set required values in `terraform/terraform.tfvars`.  に必要な設定値を入力します。
3. Run Terraform: Terraformを実行します。

```bash
cd terraform
terraform init
terraform apply
```

4. Add required GitHub Actions secrets. GitHub Actionsで使用する必要なシークレットをGitHubに設定します。
5. Push to main. `main` ブランチへプッシュします。
6. GitHub Actions builds the Docker image and deploys to EC2 through AWS Systems Manager.
   GitHub ActionsがDockerイメージを自動的にビルドし、AWS Systems Manager経由でEC2へデプロイを実行します。

notes:
本プロジェクトの一環として、追加のプロジェクト資料（システム設計、アーキテクチャに関する決定事項、
デプロイメントガイド、テスト結果、機能仕様書）が作成されており、これらは別途入手可能です。

Additional project documentation (system design, architecture decisions, deployment guide, 
testing results, and feature specification) was prepared as part of this project and is available separately.
