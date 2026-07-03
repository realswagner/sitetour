data "aws_caller_identity" "current" {}

resource "aws_iam_role_policy" "ec2_ssm_parameter_read" {
  name = "sitetour-ec2-ssm-parameter-read"
  role = aws_iam_role.sitetour_ssm_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ssm:GetParameter",
          "ssm:GetParameters",
          "ssm:GetParametersByPath"
        ]
        Resource = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/sitetour/dev/*"
      }
    ]
  })
}