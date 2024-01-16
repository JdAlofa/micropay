#!/bin/bash

docker compose down

cd common
mvn clean install
mvn package

cd ../rest-service
mvn clean install
mvn package

cd ..
docker compose up -d --build
