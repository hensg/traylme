#!/bin/bash
set -e
set -x

rm -rf build/
npm run-script build
aws s3 sync build s3://trayl-me-artifacts/frontend/traylme/build
