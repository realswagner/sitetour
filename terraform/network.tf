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

  availability_zone = var.availability_zone

  map_public_ip_on_launch = true

  tags = {
    Name = "${local.name_prefix} Public Subnet"
  }

}

//gateway configuration for outside connection
resource "aws_internet_gateway" "igw" {

  vpc_id = aws_vpc.sitetour_vpc.id

  tags = {
    Name = "${local.name_prefix} IGW"
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
    Name = "${local.name_prefix} Public Route Table"
  }

}
//route table association
resource "aws_route_table_association" "public_assoc" {

  subnet_id = aws_subnet.public_subnet.id

  route_table_id = aws_route_table.public_rt.id

}