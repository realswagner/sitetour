output "public_ip" {
  description = "Public IPv4 of the EC2 instance"
  value       = aws_instance.sitetour_server.public_ip
}

output "public_dns" {
  description = "Public DNS name of the EC2 instance"
  value       = aws_instance.sitetour_server.public_dns
}

output "application_url" {
  description = "URL of the deployed application"
  value       = "http://${aws_instance.sitetour_server.public_ip}:8080"
}

output "ssh_command" {
  description = "Convenient SSH command"
  value = "ssh -i C:/Users/USER/.ssh/sitetour ubuntu@${aws_instance.sitetour_server.public_ip}"
}