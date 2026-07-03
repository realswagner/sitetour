variable "project_name" {
  type    = string
  default = "SiteTour"
}

variable "aws_region" {
  type    = string
  default = "ap-northeast-1"
}

variable "availability_zone" {
  type    = string
  default = "ap-northeast-1a"
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}

variable "my_ip" {
  type = string
}
// parameters to be instantiated from aws param
variable "postgres_db" {
  type      = string
  sensitive = true
}

variable "postgres_user" {
  type      = string
  sensitive = true
}

variable "postgres_password" {
  type      = string
  sensitive = true
}

variable "spring_datasource_url" {
  type = string
}

variable "spring_datasource_username" {
  type      = string
  sensitive = true
}

variable "spring_datasource_password" {
  type      = string
  sensitive = true
}

variable "domain_name" {
  type = string
}

variable "app_domain" {
  type = string
}

variable "letsencrypt_email" {
  type = string
}