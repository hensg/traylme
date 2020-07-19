#!/bin/bash
set -e
set -v

export DEBIAN_FRONTEND=noninteractive

sudo apt-get update
sudo apt-get install -y nginx awscli certbot python-certbot-nginx build-essential npm

echo "Creating dirs"
sudo mkdir -p /var/www/trayl.me/
sudo chown -R admin /var/www/trayl.me/
sudo mkdir -p /var/nginx/cache
sudo chown -R admin /var/nginx/cache
sudo mkdir -p /var/logs/nginx
sudo chown -R admin /var/logs/nginx

echo "AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID" > /etc/environment
echo "AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY" >> /etc/environment

echo "S3 static files synchronizing"
#aws s3 sync s3://trayl-static-files/static /var/www/trayl.me
aws s3 sync s3://trayl-me-artifacts/frontend/traylme/build/ /var/www/trayl.me
sudo chown -R admin /var/www/trayl.me/

echo "Sync certbot ssl files"
sudo mkdir -p /etc/letsencrypt/live/trayl.me
sudo chown -R admin /etc/letsencrypt/live/trayl.me
aws s3 sync s3://trayl-secrets-files/sslcerts /etc/letsencrypt/live/trayl.me
sudo chown -R root /etc/letsencrypt/live/trayl.me

echo "Moving nginx conf files"
sudo mv confs/nginx.conf /etc/nginx/nginx.conf
sudo mv confs/http /etc/nginx/conf.d/

echo "Enabling nginx"
sudo systemctl enable nginx

#echo "Opening firewall ports 80 and 443"
#sudo ufw allow 'Nginx Full'

echo "Installation and configuration finished..."
