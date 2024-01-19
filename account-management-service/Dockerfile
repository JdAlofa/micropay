FROM eclipse-temurin:21 as jre-build
WORKDIR /usr/src
COPY target/payment-service-1.0-SNAPSHOT.jar /usr/src/
CMD sleep 10 && java -Xmx64m -jar account-management-service-1.0-SNAPSHOT.jar
