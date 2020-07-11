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

data "aws_subnet" "us_east_1a_public" {
  filter {
    name = "tag:Name"
    values = ["main-public-us-east-1a"]
  }
}

data "aws_ami" "nginx_web" {
  owners = ["self"]
  most_recent = true
  filter {
    name = "tag:Name"
    values = ["nginx-web"]
  }
}

resource "aws_security_group" "nginx" {
  tags = {
    Name = "nginx-sg"
  }
  vpc_id = data.aws_vpc.main.id
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 22
    to_port   = 22
    cidr_blocks = ["37.201.225.82/32"]
  }
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 80
    to_port   = 80
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 443
    to_port   = 443
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "web_server_1" {
  ami           = data.aws_ami.nginx_web.id
  instance_type = "t3a.nano"
  subnet_id     = data.aws_subnet.us_east_1a_public.id
  vpc_security_group_ids = [aws_security_group.nginx.id]
  key_name = "packer-ami-builder"
  tags = {
    Name = "web-server-1-us-east-1a"
  }
  credit_specification {
    cpu_credits = "standard"
  }
}

resource "aws_route53_record" "traylme" {
  zone_id = "Z07600791BDUDUR6EYMUD"
  name    = "trayl.me"
  type    = "A"
  ttl     = "300"
  records = [aws_instance.web_server_1.public_ip]
}

output "instance_ip_addr" {
  value = aws_instance.web_server_1.public_ip
}
