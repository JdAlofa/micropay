#!/bin/bash
set -e

docker-compose down

cd common
mvn clean install
mvn package

cd ../rest-service
mvn clean install
mvn package

cd ../token-service
mvn clean install
mvn package

cd ..
docker-compose up -d --build
sleep 10
cd e2e-test
mvn clean install
mvn test
