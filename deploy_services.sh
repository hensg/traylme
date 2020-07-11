#!/bin/sh

./gradlew clean build -Pversion=latest

aws s3 sync servlet-anonymous-api/build/libs/  s3://trayl-me-artifacts/releases/
aws s3 sync servlet-redirect-service/build/libs/  s3://trayl-me-artifacts/releases/
