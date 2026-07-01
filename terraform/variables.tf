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