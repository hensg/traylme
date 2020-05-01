#!/bin/bash
set -e
set -v

sleep 30 # :(

sudo apt-get update
sudo apt-get install software-properties-common
sudo add-apt-repository universe
sudo add-apt-repository ppa:certbot/certbot
sudo apt-get update

sudo apt-get install -y nginx awscli certbot python-certbot-nginx

echo "Creating dirs"
sudo mkdir -p /var/www/uushort.com/
sudo mkdir -p /var/nginx/logs
sudo mkdir -p /var/nginx/cache

echo "S3 static files synchronizing"
aws s3 sync s3://uushort-static-files/static /var/www/uushort.com

echo "Sync certbot ssl files"
sudo mkdir -p /etc/letsencrypt/live/uushort.com
aws s3 sync s3://uushort-secrets-files/sslcerts /etc/letsencrypt/live/uushort.com/
sudo chown -R root /etc/letsencrypt/live/uushort.com

echo "Moving certbot cron"
sudo mv cron_certbot /etc/cron.d/

echo "Moving nginx conf files"
sudo mv confs/nginx.conf /etc/nginx/nginx.conf
sudo mv confs/http /etc/nginx/conf.d/

echo "Enabling nginx"
sudo systemctl enable nginx

echo "Opening firewall ports 80 and 443"
sudo ufw allow 'Nginx Full'

echo "Installation and configuration finished..."
