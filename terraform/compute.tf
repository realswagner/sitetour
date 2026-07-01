// IAM role for EC2 to use AWS Systems Manager
resource "aws_iam_role" "sitetour_ssm_role" {

  name = "${local.name_prefix}-ssm-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })

}

// Attach AWS managed SSM policy to the role
resource "aws_iam_role_policy_attachment" "sitetour_ssm_policy" {

  role       = aws_iam_role.sitetour_ssm_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"

}

// Instance profile connects the IAM role to the EC2 instance
resource "aws_iam_instance_profile" "sitetour_ssm_profile" {

  name = "${local.name_prefix}-ssm-profile"
  role = aws_iam_role.sitetour_ssm_role.name

}

// ec2
resource "aws_instance" "sitetour_server" {

  ami = "ami-0d52744d6551d851e"

  instance_type = var.instance_type

  subnet_id = aws_subnet.public_subnet.id

  vpc_security_group_ids = [
    aws_security_group.sitetour_sg.id
  ]
  iam_instance_profile        = aws_iam_instance_profile.sitetour_ssm_profile.name
  associate_public_ip_address = true

  //ssh key pair
  key_name = aws_key_pair.sitetour_key.key_name

  user_data = file("${path.module}/bootstrap.sh")

  tags = {
    Name = "${local.name_prefix} Server"
    App  = "sitetour"
    Env  = "dev"
  }

}

resource "aws_key_pair" "sitetour_key" {

  key_name = "sitetour-key"

  public_key = file("C:/Users/USER/.ssh/sitetour.pub")

}