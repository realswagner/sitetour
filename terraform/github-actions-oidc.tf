resource "aws_iam_openid_connect_provider" "github" {

  url = "https://token.actions.githubusercontent.com"

  client_id_list = [
    "sts.amazonaws.com"
  ]

  thumbprint_list = [
    "6938fd4d98bab03faadb97b34396831e3780aea1"
  ]

}

resource "aws_iam_role" "github_actions_deploy_role" {

  name = "${local.name_prefix}-github-actions-deploy-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Federated = aws_iam_openid_connect_provider.github.arn
        }
        Action = "sts:AssumeRoleWithWebIdentity"
        Condition = {
          StringEquals = {
            "token.actions.githubusercontent.com:aud" = "sts.amazonaws.com"
          }
          StringLike = {
            "token.actions.githubusercontent.com:sub" = "repo:realswagner/sitetour:ref:refs/heads/main"
          }
        }
      }
    ]
  })

}

resource "aws_iam_role_policy" "github_actions_ssm_deploy_policy" {

  name = "${local.name_prefix}-github-actions-ssm-deploy-policy"
  role = aws_iam_role.github_actions_deploy_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ssm:SendCommand",
          "ssm:GetCommandInvocation"
        ]
        Resource = [
          aws_instance.sitetour_server.arn,
          "arn:aws:ssm:${var.aws_region}:*:document/AWS-RunShellScript"
        ]
      },
      {
        Effect = "Allow"
        Action = [
          "ec2:DescribeInstances"
        ]
        Resource = "*"
      }
    ]
  })

}