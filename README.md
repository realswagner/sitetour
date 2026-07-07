# SiteTour Interview Management System

# Overview

Java・Spring Boot・AWSを用いて開発した面接管理システムです。
Spring BootによるWebアプリケーション開発だけでなく、
Terraform・Docker・GitHub Actions・AWSを利用したインフラ構築およびCI/CDまでを一貫して実装しました。

SiteTour is an interview management system built with Spring Boot and AWS.
In addition to developing the web application itself, this project demonstrates infrastructure automation,
CI/CD, secure configuration management, and cloud deployment using Terraform, Docker, GitHub Actions, and AWS.

# Live Demo
https://sitetour.realswagner.dev
(insert dashboard pic)

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

(insert image)

デプロイ手順 Quick Deployment

1. TerraformでAWS環境を構築 ・　Provision AWS infrastructure using Terraform

2. AWS Parameter Storeへ設定値を登録　・　Configure AWS Parameter Store.

3. GitHub Secretsを設定・ Configure GitHub Secrets.

4. mainブランチへPush　・　Push to the main branch.

5. GitHub Actionsが自動デプロイを実行　・GitHub Actions automatically deploys the latest version.
