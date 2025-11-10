FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
VOLUME /tmp
EXPOSE 8761

# jar correto do eureka (gera após mvn package)
ADD ./target/hr-eureka-server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]