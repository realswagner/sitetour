resource "aws_ebs_volume" "postgres_data" {
  availability_zone = aws_instance.sitetour_server.availability_zone
  size              = var.postgres_volume_size
  type              = "gp3"

  tags = {
    Name = "sitetour-dev-postgres-data"
    App  = "sitetour"
    Env  = "dev"
  }

  lifecycle {
    prevent_destroy = true
  }
}

resource "aws_volume_attachment" "postgres_data" {
  device_name = "/dev/sdf"
  volume_id   = aws_ebs_volume.postgres_data.id
  instance_id = aws_instance.sitetour_server.id
}