cd common
mvn clean install && mvn package

cd ../rest-service
mvn clean install

cd ../e2e-test
mvn clean install

cd ../token-service
mvn clean install
