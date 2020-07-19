#!/bin/sh
set -e
set -x

export DEBIAN_FRONTEND=noninteractive

groupadd mysql && useradd -r -g mysql -s /bin/false mysql

apt-get update && apt-get install -y \
    pwgen \
    openssl \
    perl \
    wget \
    lsb-release \
    gnupg \
    libaio1 \
    cron \
    vim \
    awscli

wget https://dev.mysql.com/get/mysql-apt-config_0.8.15-1_all.deb
dpkg -i mysql-apt-config_*

apt-get update && apt-get install -y \
  mysql-server \
  mysql-client

rm -rf /opt/mysql/*
rm -rf /var/lib/mysql/*
rm -rf /etc/mysql/*
rm -rf /var/log/mysql/*
rm -rf /var/run/mysqld/*

mkdir -p /opt/mysql/backups
chown -R mysql:mysql /opt/mysql/backups
chmod u=wrx,g=wr /opt/mysql/backups

mkdir -p /opt/mysql/data
chown -R mysql:mysql /opt/mysql/data
chmod u=wrx,g=wr /opt/mysql/data

mkdir -p /var/log/mysql/
chown -R mysql:mysql /var/log/mysql/

mkdir -p /var/run/mysql/
chown -R mysql:mysql /var/run/mysql/

mkdir -p /etc/mysql
chown -R mysql:mysql /etc/mysql

mkdir -p /etc/keys/mysql
chown -R mysql:mysql /etc/keys/mysql

echo "Pulling s3 certs..."
aws s3 sync s3://trayl-secrets-files/mysql /opt/mysql/certs

chown -R mysql:mysql /opt/mysql/certs
