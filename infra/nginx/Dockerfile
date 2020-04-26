FROM nginx

ADD static /data/static

ADD nginx.conf /etc/nginx/

ADD certs /etc/ssl/certs

RUN mkdir -p /var/nginx/logs/

EXPOSE 80 443
