#!/bin/bash
set -e
set -x

export DEBIAN_FRONTEND=noninteractive

apt update -y
apt-get update && apt-get install -y awscli cron default-jre vim telnet wget

wget https://downloads.apache.org/kafka/2.5.0/kafka_2.12-2.5.0.tgz

tar -zxvf kafka_2.12-2.5.0.tgz

mv kafka_2.12-2.5.0 /opt/

ln -s /opt/kafka_2.12-2.5.0 /opt/kafka

echo "AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID" > /etc/environment
echo "AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY" >> /etc/environment
echo "SSL_KEYSTORE_PASS=$SSL_KEYSTORE_PASS" >> /etc/environment

aws s3 cp s3://trayl-secrets-files/mysql-client/client-keystore.p12 /home/admin/client-keystore.p12
