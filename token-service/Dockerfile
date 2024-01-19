FROM eclipse-temurin:21 as jre-build
WORKDIR /usr/src
COPY target/token-service-1.0-SNAPSHOT.jar /usr/src/
CMD sleep 10 && java -Xmx64m -jar token-service-1.0-SNAPSHOT.jar