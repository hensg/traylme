#!/bin/bash

set -euox

echo "Pulling artifacts.."
aws s3 sync s3://trayl-me-artifacts/releases/ /home/admin/

function restart_service {
  echo "$1 has changed, restarting it..."
  md5sum "/home/admin/$1-latest.jar" > "/home/admin/$1.md5"
  sudo service "$1" restart
}

function restart_if_needed {
  echo "Checking whether $1 jar has changed or not..."
  FIRST_DEPLOY=true
  if [ -f "/home/admin/$1.md5" ]
  then
    FIRST_DEPLOY=false
  fi

  PREVIOUS_MD5=$(cat "/home/admin/$1.md5" | awk '{print $1}')
  CURRENT_JAR_MD5=$(md5sum "/home/admin/$1-latest.jar" | awk '{print $1}')

  if [ "$FIRST_DEPLOY" == true ] || [ "$PREVIOUS_MD5" != "$CURRENT_JAR_MD5" ]
  then
     restart_service "$1"
  fi
}

restart_if_needed redirect-count-stream
