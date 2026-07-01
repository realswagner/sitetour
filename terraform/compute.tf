

// ec2
resource "aws_instance" "sitetour_server" {

  ami           = "ami-0d52744d6551d851e"

  instance_type = var.instance_type

  subnet_id = aws_subnet.public_subnet.id

  vpc_security_group_ids = [
    aws_security_group.sitetour_sg.id
  ]

  associate_public_ip_address = true

  //ssh key pair
  key_name = aws_key_pair.sitetour_key.key_name

  user_data = file("${path.module}/bootstrap.sh")

  tags = {
    Name = "${local.name_prefix} Server"
  }

}

resource "aws_key_pair" "sitetour_key" {

  key_name = "sitetour-key"

  public_key = file("C:/Users/USER/.ssh/sitetour.pub")

}