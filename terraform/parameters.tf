resource "aws_ssm_parameter" "postgres_db" {
  name  = "/sitetour/dev/POSTGRES_DB"
  type  = "SecureString"
  value = var.postgres_db
}

resource "aws_ssm_parameter" "postgres_user" {
  name  = "/sitetour/dev/POSTGRES_USER"
  type  = "SecureString"
  value = var.postgres_user
}

resource "aws_ssm_parameter" "postgres_password" {
  name  = "/sitetour/dev/POSTGRES_PASSWORD"
  type  = "SecureString"
  value = var.postgres_password
}

resource "aws_ssm_parameter" "spring_datasource_url" {
  name  = "/sitetour/dev/SPRING_DATASOURCE_URL"
  type  = "String"
  value = var.spring_datasource_url
}

resource "aws_ssm_parameter" "spring_datasource_username" {
  name  = "/sitetour/dev/SPRING_DATASOURCE_USERNAME"
  type  = "SecureString"
  value = var.spring_datasource_username
}

resource "aws_ssm_parameter" "spring_datasource_password" {
  name  = "/sitetour/dev/SPRING_DATASOURCE_PASSWORD"
  type  = "SecureString"
  value = var.spring_datasource_password
}

resource "aws_ssm_parameter" "domain_name" {
  name  = "/sitetour/dev/DOMAIN_NAME"
  type  = "String"
  value = var.domain_name
}

resource "aws_ssm_parameter" "app_domain" {
  name  = "/sitetour/dev/APP_DOMAIN"
  type  = "String"
  value = var.app_domain
}

resource "aws_ssm_parameter" "letsencrypt_email" {
  name  = "/sitetour/dev/LETSENCRYPT_EMAIL"
  type  = "String"
  value = var.letsencrypt_email
}