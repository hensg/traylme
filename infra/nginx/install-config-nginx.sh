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
sudo mkdir -p /var/www/trayl.me/
sudo mkdir -p /var/nginx/cache
sudo mkdir -p /var/logs/nginx

echo "S3 static files synchronizing"
aws s3 sync s3://trayl-static-files/static /var/www/trayl.me

echo "Sync certbot ssl files"
sudo mkdir -p /etc/letsencrypt/live/trayl.me
aws s3 sync s3://trayl-secrets-files/sslcerts /etc/letsencrypt/live/trayl.me
sudo chown -R root /etc/letsencrypt/live/trayl.me

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
