FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ADD ./target/hr-oauth-0.0.1-SNAPSHOT.jar hr-oauth.jar
ENTRYPOINT ["java","-jar","/hr-oauth.jar"]