#!/bin/sh
set -e
set -x

MASTER_HOST=$1

while ! mysqladmin ping \
  -h $MASTER_HOST -urepl -prepl \
  --ssl-mode=REQUIRED \
  ; do
  >&2 echo "Master unavailable - sleeping"
  sleep 5
done

>&2 echo "MySQL master is running"

MASTER_STATUS=`mysql -h$MASTER_HOST -urepl -prepl --ssl-mode=REQUIRED -s -e "show master status" | tail -n1`;

MASTER_LOG_FILE=`echo $MASTER_STATUS | awk {'print $1'}`
MASTER_LOG_POS=`echo $MASTER_STATUS | awk {'print $2'}`

echo "Cleaning data"
rm -rf /opt/mysql/data

echo "Initializing slave..."
mysqld --user=mysql --initialize --server-id=2 --init-file=/data/scripts/slave.sql --verbose --log-error-verbosity=3

echo "Setting master to $MASTER_HOST, log file $MASTER_LOG_FILE at position $MASTER_LOG_POS"
sleep 3

mysqld --user=mysql --server-id=2

mysql -uroot -proot << EOF
  SET GLOBAL server_id = 2;

  CHANGE MASTER TO
    MASTER_HOST="$MASTER_HOST",
    MASTER_PORT=3306,
    MASTER_USER='repl',
    MASTER_PASSWORD='repl',
    MASTER_LOG_FILE="$MASTER_LOG_FILE",
    MASTER_LOG_POS=$MASTER_LOG_POS,
    MASTER_CONNECT_RETRY=5,
    REQUIRE_ROW_FORMAT=1,
    MASTER_SSL=1;

  START SLAVE;
EOF
