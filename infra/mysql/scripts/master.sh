#!/bin/sh

set -e
set -x

echo "Initializing mysql"
mysqld --user=mysql --server-id=1 --initialize --init-file=/data/scripts/master.sql --log-error-verbosity=3

echo "Initilization finished, starting service"
systemctl enable mysql
service mysql start
