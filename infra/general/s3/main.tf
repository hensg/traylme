provider "aws" {
  version = "~> 2.0"
  region  = "us-east-1"
}

resource "aws_s3_bucket" "trayl_static_files" {
  bucket = "trayl-static-files"
  acl    = "private"
  tags = {
    Name        = "trayl-static-files"
    Environment = "web"
  }
}

resource "aws_s3_bucket" "trayl_secrets_files" {
  bucket = "trayl-secrets-files"
  acl    = "private"
  tags = {
    Name        = "trayl-secrets-files"
    Environment = "web"
  }
}
