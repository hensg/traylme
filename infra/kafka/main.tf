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

data "aws_ami" "kafka" {
  owners = ["self"]
  most_recent = true
  filter {
    name = "tag:Name"
    values = ["kafka"]
  }
}

resource "aws_security_group" "kafka" {
  tags = {
    Name = "kafka"
  }
  vpc_id = data.aws_vpc.main.id
  ingress {
    protocol  = "-1"
    self      = true
    from_port = 0
    to_port   = 0
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block]
  }
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 9092
    to_port   = 9092
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block]
  }
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 2181
    to_port   = 2181
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block]
  }
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 22
    to_port   = 22
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    protocol  = "-1"
    from_port = 0
    to_port   = 0
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block, "0.0.0.0/0"]
  }
}

resource "aws_instance" "kafka_01" {
  ami           = data.aws_ami.kafka.id
  instance_type = "t3a.micro"
  subnet_id     = data.aws_subnet.us_east_1a_private.id
  vpc_security_group_ids = [aws_security_group.kafka.id]
  key_name = "packer-ami-builder"

  tags = {
    Name = "kafka-01"
  }

  credit_specification {
    cpu_credits = "standard"
  }

  root_block_device {
    volume_size = 20
  }

  provisioner "file" {
    source = "config/server.properties"
    destination = "/tmp/server.properties"
    connection {
      type = "ssh"
      host = aws_instance.kafka_01.private_ip
      user = "admin"
      private_key = file("~/.ssh/packer-ami-builder.pem")
    }
  }

  provisioner "remote-exec" {
    connection {
      type = "ssh"
      host = aws_instance.kafka_01.private_ip
      user = "admin"
      private_key = file("~/.ssh/packer-ami-builder.pem")
    }

    inline = [
      "sudo mv /tmp/server.properties /opt/kafka/config/server.properties",
      "sudo systemctl daemon-reload",
      "sudo systemctl enable zookeeper",
      "sudo systemctl start zookeeper",
      "sudo systemctl enable kafka-server",
      "sudo systemctl start kafka-server",
      "sudo systemctl start redirect-count-stream"
    ]
  }
}

data "aws_route53_zone" "traylme" {
  name = "trayl.me"
}

resource "aws_route53_record" "anon_api" {
  zone_id         = data.aws_route53_zone.traylme.zone_id
  name            = "kafka-01.trayl.me"
  ttl             = "60"
  type            = "A"
  records         = [aws_instance.kafka_01.private_ip]
}
