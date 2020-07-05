FROM nginx

ADD install-config-nginx.sh .

RUN apt-get update && \
    apt-get install -y nginx awscli certbot python-certbot-nginx && \
    mkdir -p /var/www/uushort.com/ && \
    mkdir -p /var/logs/nginx && \
    mkdir -p /var/nginx/cache && \
    mkdir -p /etc/nginx/ && \
    mkdir -p /etc/nginx/jwt && \
    mkdir -p /etc/letsencrypt/live/uushort.com

ADD confs/nginx.conf /etc/nginx/nginx.conf
ADD confs/http /etc/nginx/conf.d/

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY

ENV AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY

RUN echo $AWS_ACCESS_KEY_ID
RUN echo $AWS_SECRET_ACCESS_KEY

RUN aws s3 sync s3://trayl-static-files/static/ /var/www/trayl.me && \
    aws s3 sync s3://trayl-secrets-files/sslcerts/ /etc/letsencrypt/live/trayl.me/ && \
    chown -R root /etc/letsencrypt/live/trayl.me

RUN ls -l /etc/letsencrypt/live/trayl.me
EXPOSE 80 443

RUN nginx -t
