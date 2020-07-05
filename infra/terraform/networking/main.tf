provider "aws" {
  version = "~> 2.0"
  region  = "us-east-1"
}

resource "aws_vpc" "main" {
  cidr_block = "10.1.0.0/16"
  tags = {
    Name = "main-private"
  }
}

resource "aws_subnet" "us_east_1a" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.1.1.0/24"
  availability_zone = "us-east-1a"
  tags = {
    Name = "main-us-east-1a"
  }
}

resource "aws_vpc" "main_public" {
  cidr_block = "172.1.0.0/16"
  tags = {
    Name = "main-public"
  }
}
resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.main_public.id
  tags = {
    Name = "main-igw"
  }
}
resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.main_public.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }
  tags = {
    Name = "main-public-route-table"
  }
}
resource "aws_subnet" "us_east_1a_public" {
  vpc_id            = "${aws_vpc.main_public.id}"
  cidr_block        = "172.1.1.0/24"
  availability_zone = "us-east-1a"
  map_public_ip_on_launch = true
  tags = {
    Name = "main-public-us-east-1a"
  }
}
resource "aws_route_table_association" "public_route_table_association" {
  subnet_id      = aws_subnet.us_east_1a_public.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_security_group" "ssh" {
  tags = {
    Name = "ssh"
  }
  vpc_id = "${aws_vpc.main_public.id}"
  ingress {
    protocol  = "tcp"
    self      = true
    from_port = 22
    to_port   = 22
    cidr_blocks = ["37.201.225.0/28"]
  }
}


resource "aws_security_group" "web" {
  tags = {
    Name = "web"
  }
  vpc_id = "${aws_vpc.main_public.id}"
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
