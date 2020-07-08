provider "aws" {
  version = "~> 2.0"
  region  = "us-east-1"
}

data "aws_vpc" "main" {
  filter {
    name = "tag:Name"
    values = ["main"]
  }
}

data "aws_subnet" "us_east_1a_private" {
  filter {
    name = "tag:Name"
    values = ["main-us-east-1a"]
  }
}

data "aws_subnet" "us_east_1a_public" {
  filter {
    name = "tag:Name"
    values = ["main-public-us-east-1a"]
  }
}

data "aws_ami" "backend_services" {
  owners = ["self"]
  most_recent = true
  filter {
    name = "tag:Name"
    values = ["backend-services"]
  }
}

resource "aws_security_group" "services" {
  tags = {
    Name = "services"
  }
  vpc_id = data.aws_vpc.main.id
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 8080
    to_port   = 8081
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block, data.aws_subnet.us_east_1a_public.cidr_block]
  }
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 22
    to_port   = 22
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    protocol  = "-1"
    self      = true
    from_port = 0
    to_port   = 0
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block]
  }
  egress {
    protocol  = "-1"
    from_port = 0
    to_port   = 0
    cidr_blocks = ["0.0.0.0/0",data.aws_subnet.us_east_1a_private.cidr_block]
  }
  egress {
    protocol  = "tcp"
    from_port = 22
    to_port   = 22
    cidr_blocks = ["10.10.0.0/22"]
  }
}

resource "aws_instance" "backend_services" {
  ami           = data.aws_ami.backend_services.id
  instance_type = "t3a.small"
  subnet_id     = data.aws_subnet.us_east_1a_private.id
  vpc_security_group_ids = [aws_security_group.services.id]
  key_name = "packer-ami-builder"

  tags = {
    Name = "backend-services"
  }

  credit_specification {
    cpu_credits = "standard"
  }

  root_block_device {
    volume_size = 20
  }

  provisioner "remote-exec" {
    connection {
      type = "ssh"
      host = aws_instance.backend_services.private_ip
      user = "admin"
      private_key = file("~/.ssh/packer-ami-builder.pem")
    }

    inline = [
      "/bin/sh /home/admin/auto_deploy.sh",
      "sudo systemctl enable anonymous-api",
      "sudo systemctl start anonymous-api",
      "sudo systemctl enable redirect-service",
      "sudo systemctl start redirect-service"
    ]
  }
}

data "aws_route53_zone" "traylme" {
  name = "trayl.me"
}

resource "aws_route53_record" "anon_api" {
  zone_id         = data.aws_route53_zone.traylme.zone_id
  name            = "anonymousapi.trayl.me"
  ttl             = "60"
  type            = "A"
  records         = [aws_instance.backend_services.private_ip]
}

resource "aws_route53_record" "redirect_service" {
  zone_id         = data.aws_route53_zone.traylme.zone_id
  name            = "redirect.trayl.me"
  ttl             = "60"
  type            = "A"
  records         = [aws_instance.backend_services.private_ip]
}
