FROM nginx

ADD install-config-nginx.sh .

RUN apt-get update && \
    apt-get install -y nginx awscli certbot python-certbot-nginx && \
    mkdir -p /var/www/uushort.com/ && \
    mkdir -p /var/nginx/logs && \
    mkdir -p /var/nginx/cache && \
    mkdir -p /etc/nginx/ && \
    mkdir -p /etc/letsencrypt/live/uushort.com

ADD confs/nginx.conf /etc/nginx/nginx.conf
ADD confs/http /etc/nginx/conf.d/

ARG aws_access_key_id
ARG aws_secret_access_key

ENV AWS_ACCESS_KEY_ID=$aws_access_key_id
ENV AWS_SECRET_ACCESS_KEY=$aws_secret_access_key

RUN echo $AWS_ACCESS_KEY_ID
RUN echo $AWS_SECRET_ACCESS_KEY

RUN aws s3 sync s3://uushort-static-files/static /var/www/uushort.com && \
    aws s3 sync s3://uushort-secrets-files/sslcerts /etc/letsencrypt/live/uushort.com/ && \
    chown -R root /etc/letsencrypt/live/uushort.com

EXPOSE 80 443

RUN nginx -t

#CMD ["/bin/bash", "-c", "aws s3 sync s3://uushort-static-files/static /var/www/uushort.com && aws s3 sync s3://uushort-secrets-files/sslcerts /etc/letsencrypt/live/uushort.com/ && chown -R root /etc/letsencrypt/live/uushort.com && exec nginx -g 'daemon off;'"]

# CMD ["/bin/bash", "-c", "nginx-debug", "-g", "'daemon off;'"]
