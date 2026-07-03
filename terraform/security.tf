

//security group config
resource "aws_security_group" "sitetour_sg" {

  name = "${local.name_prefix} Security Group"

  description = "Allow SSH and Spring Boot"

  vpc_id = aws_vpc.sitetour_vpc.id

  ingress {

    description = "SSH"

    from_port = 22

    to_port = 22

    protocol = "tcp"

    cidr_blocks = [var.my_ip]

  }

  // HTTP for Nginx
  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  //open port 443 for https configuration to custom domain
  ingress {
    description = "HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {

    from_port = 0

    to_port = 0

    protocol = "-1"

    cidr_blocks = ["0.0.0.0/0"]

  }

  tags = {

    Name = "${local.name_prefix} SG"

  }

}