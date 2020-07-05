provider "aws" {
  version = "~> 2.0"
  region  = "us-east-1"
}

data "aws_subnet" "us_east_1a_public" {
  filter {
    name = "tag:Name"
    values = ["main-public-us-east-1a"]
  }
}
data "aws_security_group" "web" {
  filter {
    name = "tag:Name"
    values = ["web"]
  }
}
data "aws_security_group" "ssh" {
  filter {
    name = "tag:Name"
    values = ["ssh"]
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

resource "aws_instance" "web_server_1" {
  ami           = data.aws_ami.nginx_web.id
  instance_type = "t3a.nano"
  subnet_id     = data.aws_subnet.us_east_1a_public.id
  vpc_security_group_ids = [data.aws_security_group.web.id,data.aws_security_group.ssh.id]
  key_name = "packer-ami-builder"
  tags = {
    Name = "web-server-1-us-east-1a"
  }
  credit_specification {
    cpu_credits = "standard"
  }
}

resource "aws_route53_record" "traylme" {
  zone_id = "Z0146068UQ2X4OWA6O9B"
  name    = "trayl.me"
  type    = "A"
  ttl     = "300"
  records = [aws_instance.web_server_1.public_ip]
}

output "instance_ip_addr" {
  value = aws_instance.web_server_1.public_ip
}
