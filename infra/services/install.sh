#!/bin/sh
set -e
set -x

export DEBIAN_FRONTEND=noninteractive

apt-get update && apt-get install -y awscli cron default-jre vim

echo "export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID" > /etc/environment
echo "export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY" >> /etc/environment
echo "export SSL_KEYSTORE_PASS=$SSL_KEYSTORE_PASS" >> /etc/environment

aws s3 cp s3://trayl-secrets-files/mysql-client/client-keystore.p12 /home/admin/client-keystore.p12

echo '*/5 * * * * admin /bin/sh /home/admin/auto_deploy.sh' >> /etc/crontab
