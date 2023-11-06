terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "eu-central-1"
  #  access_key = comes from environment variable
  #  secret_key = comes from environment variable
}

resource "aws_eks_node_group" "mq" {
  cluster_name    = "gg"
  node_group_name = "mq"
  node_role_arn   = local.node_role_arn
  subnet_ids      = local.subnetIds
  count           = 1
  instance_types  = ["t2.medium"]
  disk_size       = 10
  labels          = { name = "mq" }
  scaling_config {
    desired_size = 1
    max_size     = 1
    min_size     = 1
  }
}

resource "aws_eks_node_group" "ui" {
  cluster_name    = "gg"
  node_group_name = "ui"
  node_role_arn   = local.node_role_arn
  subnet_ids      = local.subnetIds
  count           = 1
  instance_types  = ["t2.small"]
  disk_size       = 6
  labels          = { name = "ui" }
  scaling_config {
    desired_size = 1
    max_size     = 1
    min_size     = 1
  }
}

resource "aws_eks_node_group" "spark_driver" {
  cluster_name    = "gg"
  node_group_name = "spark_driver"
  node_role_arn   = local.node_role_arn
  subnet_ids      = local.subnetIds
  count           = 1
  instance_types  = ["t2.medium"]
  disk_size       = 8
  labels          = { name = "spark_driver" }
  scaling_config {
    desired_size = 1
    max_size     = 1
    min_size     = 1
  }
}

resource "aws_eks_node_group" "spark_executor" {
  cluster_name    = "gg"
  node_group_name = "spark_executor"
  node_role_arn   = local.node_role_arn

  subnet_ids = local.subnetIds
  remote_access {
    ec2_ssh_key = "myaws"
  }
  count          = 1
  instance_types = ["t2.small"]
  disk_size      = 6

  labels = { name = "spark_executor" }

  scaling_config {
    desired_size = 4
    max_size     = 5
    min_size     = 1
  }

}


resource "aws_eks_node_group" "loader" {
  cluster_name    = "gg"
  node_group_name = "loader"
  node_role_arn   = local.node_role_arn
  subnet_ids      = local.subnetIds
  count           = 1
  instance_types  = ["t2.small"]
  disk_size       = 8
  labels          = { name = "loader" }
  scaling_config {
    desired_size = 1
    max_size     = 1
    min_size     = 1
  }

}

resource "aws_efs_file_system" "geocachegrouping-spark-checkpoint-fs" {
}

resource "aws_efs_mount_target" "geocachegrouping-spark-checkpoint-mt-1" {
  file_system_id = aws_efs_file_system.geocachegrouping-spark-checkpoint-fs.id
  subnet_id      = "subnet-06024c2aa7e1e9b4d"
}
resource "aws_efs_mount_target" "geocachegrouping-spark-checkpoint-mt-2" {
  file_system_id = aws_efs_file_system.geocachegrouping-spark-checkpoint-fs.id
  subnet_id      = "subnet-090baf61dcd4eef45"
}
resource "aws_efs_mount_target" "geocachegrouping-spark-checkpoint-mt-3" {
  file_system_id = aws_efs_file_system.geocachegrouping-spark-checkpoint-fs.id
  subnet_id      = "subnet-0b6228f113b8ca26b"
}

resource "aws_s3_bucket" "geocaching-s3-bucket" {
  bucket = "geocaching-s3-bucket"

}

resource "aws_ecr_repository" "geocachinggroupingrepository" {
  name         = "geocachinggroupingrepository"
  force_delete = true
}

locals {
  subnetIds = [
    "subnet-0d313cebbacae50e6", "subnet-090baf61dcd4eef45", "subnet-0bc026ad42aacd01a",
    "subnet-06024c2aa7e1e9b4d", "subnet-0d7d1c8ef9aa793d2", "subnet-0b6228f113b8ca26b"
  ]

  node_role_arn = "arn:aws:iam::281741967817:role/eksctl-gg-nodegroup-mq-NodeInstanceRole-Czt2FHColoUj"
}
