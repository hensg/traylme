#!/bin/sh
set -e
set -x

#TODO: remake this script

echo "Pulling artifacts.."
aws s3 sync s3://trayl-me-artifacts/releases/ /home/admin/

echo "Checking if anonymous api jar has changed.."
if [ ! -f /home/admin/serlet-anonymous-api.md5 ] || [`cat /home/admin/servlet-anonymous-api.md5` != `md5sum /home/admin/servlet-anonymous-api-latest.jar` ]; then
  echo "Anonymous api has changed, restarting it..."

  md5sum /home/admin/servlet-anonymous-api-latest.jar > /home/admin/servlet-anonymous-api.md5
  sudo service anonymous-api restart
fi

echo "Checking if redirect service jar has changed.."
if [ ! -f /home/admin/servlet-redirect-service.md5 ] || [`cat /home/admin/servlet-redirect-service.md5` != `md5sum /home/admin/servlet-redirect-service-latest.jar` ]; then
  echo "Redirect service has changed... restarting it"
  md5sum /home/admin/servlet-redirect-service-latest.jar > /home/admin/servlet-redirect-service.md5
  sudo service redirect-service restart
fi
