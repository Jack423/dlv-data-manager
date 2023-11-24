#!/usr/bin/env bash

TAG=$(cat target/version.txt)

docker image remove jack423/dlv-data-manager
mvn clean package -Pproduction
docker build . --platform linux/amd64 -t "dlv-data-manager:${TAG}"
docker tag dlv-data-manager:1.0.0 jack423/dlv-data-manager
docker push jack423/dlv-data-manager