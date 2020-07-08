FROM debian:10

ARG AWS_ACCESS_KEY_ID
ENV AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
RUN echo $AWS_ACCESS_KEY_ID

ARG AWS_SECRET_ACCESS_KEY
ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY

WORKDIR /home/backend

ADD install.sh .
ADD auto_deploy.sh .

ADD init.d/* /etc/init.d/

RUN /home/backend/install.sh
