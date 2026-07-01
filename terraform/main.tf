resource "aws_vpc" "sitetour_vpc" {

  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "SiteTour VPC"
  }

}

//main vpc configuration
resource "aws_subnet" "public_subnet" {

  vpc_id = aws_vpc.sitetour_vpc.id

  cidr_block = "10.0.1.0/24"

  availability_zone = "ap-northeast-1a"

  map_public_ip_on_launch = true

  tags = {
    Name = "SiteTour Public Subnet"
  }

}

//gateway configuration for outside connection
resource "aws_internet_gateway" "igw" {

  vpc_id = aws_vpc.sitetour_vpc.id

  tags = {
    Name = "SiteTour IGW"
  }

}
//route table
resource "aws_route_table" "public_rt" {

  vpc_id = aws_vpc.sitetour_vpc.id

  route {

    cidr_block = "0.0.0.0/0"

    gateway_id = aws_internet_gateway.igw.id

  }

  tags = {
    Name = "SiteTour Public Route Table"
  }

}
//route table association
resource "aws_route_table_association" "public_assoc" {

  subnet_id = aws_subnet.public_subnet.id

  route_table_id = aws_route_table.public_rt.id

}

//security group config
resource "aws_security_group" "sitetour_sg" {

  name = "SiteTour Security Group"

  description = "Allow SSH and Spring Boot"

  vpc_id = aws_vpc.sitetour_vpc.id

  ingress {

    description = "SSH"

    from_port = 22

    to_port = 22

    protocol = "tcp"

    cidr_blocks = ["220.146.89.148/32"]

  }

  ingress {

    description = "Spring Boot"

    from_port = 8080

    to_port = 8080

    protocol = "tcp"

    cidr_blocks = ["0.0.0.0/0"]

  }

  egress {

    from_port = 0

    to_port = 0

    protocol = "-1"

    cidr_blocks = ["0.0.0.0/0"]

  }

  tags = {

    Name = "SiteTour SG"

  }

}

// ec2
resource "aws_instance" "sitetour_server" {

  ami           = "ami-0d52744d6551d851e"

  instance_type = "t3.micro"

  subnet_id = aws_subnet.public_subnet.id

  vpc_security_group_ids = [
    aws_security_group.sitetour_sg.id
  ]

  associate_public_ip_address = true

  //ssh key pair
  key_name = aws_key_pair.sitetour_key.key_name

  tags = {
    Name = "SiteTour Server"
  }

}

resource "aws_key_pair" "sitetour_key" {

  key_name = "sitetour-key"

  public_key = file("C:/Users/USER/.ssh/sitetour.pub")

}