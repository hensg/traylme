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

data "aws_ami" "mysql" {
  owners = ["self"]
  most_recent = true
  filter {
    name = "tag:Name"
    values = ["mysql"]
  }
}

resource "aws_security_group" "db" {
  tags = {
    Name = "db"
  }
  vpc_id = data.aws_vpc.main.id
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 3306
    to_port   = 3306
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block]
  }
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 22
    to_port   = 22
    cidr_blocks = ["10.10.0.0/22"] #vpn
  }
  ingress {
    protocol  = "-1"
    self      = true
    from_port = 0
    to_port   = 0
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block] #all trafic inside subnet
  }
  egress {
    protocol  = "tcp"
    from_port = 3306
    to_port   = 3306
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block]
  }
  egress {
    protocol  = "-1"
    from_port = 0
    to_port   = 0
    cidr_blocks = [data.aws_subnet.us_east_1a_private.cidr_block] #all trafic inside subnet
  }
}

resource "aws_instance" "mysql_master" {
  ami           = data.aws_ami.mysql.id
  instance_type = "t3a.small"
  subnet_id     = data.aws_subnet.us_east_1a_private.id
  vpc_security_group_ids = [aws_security_group.db.id]
  key_name = "packer-ami-builder"

  tags = {
    Name = "mysql-master"
  }

  credit_specification {
    cpu_credits = "standard"
  }

  root_block_device {
    volume_size = 15
  }

  provisioner "file" {
    source = "./scripts/master.sh"
    destination = "/tmp/master.sh"
    connection {
      type = "ssh"
      host = aws_instance.mysql_master.private_ip
      user = "admin"
      private_key = file("~/.ssh/packer-ami-builder.pem")
    }
  }

  provisioner "remote-exec" {
    connection {
      type = "ssh"
      host = aws_instance.mysql_master.private_ip
      user = "admin"
      private_key = file("~/.ssh/packer-ami-builder.pem")
    }

    inline = [
      "sudo hostnamectl set-hostname mysql-master.trayl.me",
      "echo '127.0.0.1  mysql-master.trayl.me' >> /etc/hosts",
      "chmod +x /tmp/master.sh",
      "sudo /bin/sh -c '/tmp/master.sh'"
    ]
  }
}

data "aws_route53_zone" "traylme" {
  name = "trayl.me"
}

resource "aws_route53_record" "mysql_master_dns" {
  zone_id         = data.aws_route53_zone.traylme.zone_id
  name            = "mysql-master.trayl.me"
  ttl             = "60"
  type            = "A"
  records         = [aws_instance.mysql_master.private_ip]
}

resource "aws_instance" "mysql_slave_1" {
  ami           = data.aws_ami.mysql.id
  instance_type = "t3a.small"
  subnet_id     = data.aws_subnet.us_east_1a_private.id
  vpc_security_group_ids = [aws_security_group.db.id]
  key_name = "packer-ami-builder"

  tags = {
    Name = "mysql-slave-1"
  }

  credit_specification {
    cpu_credits = "standard"
  }

  root_block_device {
    volume_size = 15
  }

  provisioner "file" {
    source = "./scripts/slave.sh"
    destination = "/tmp/slave.sh"
    connection {
      type = "ssh"
      host = aws_instance.mysql_slave_1.private_ip
      user = "admin"
      private_key = file("~/.ssh/packer-ami-builder.pem")
    }
  }

  provisioner "remote-exec" {
    connection {
      type = "ssh"
      host = aws_instance.mysql_slave_1.private_ip
      user = "admin"
      private_key = file("~/.ssh/packer-ami-builder.pem")
    }

    inline = [
      "sudo hostnamectl set-hostname mysql-slave.trayl.me",
      "echo '127.0.0.1  mysql-slave.trayl.me' >> /etc/hosts",
      "chmod +x /tmp/slave.sh",
      "sudo /bin/sh -c '/tmp/slave.sh mysql-master.trayl.me'"
    ]
  }
}

resource "aws_route53_record" "mysql_slave_1_dns" {
  zone_id         = data.aws_route53_zone.traylme.zone_id
  name            = "mysql-slave-1.trayl.me"
  ttl             = "300"
  type            = "A"
  records         = [aws_instance.mysql_slave_1.private_ip]
}

