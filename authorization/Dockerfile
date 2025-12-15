FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY /target/authorization-0.0.1-SNAPSHOT.jar /app/authorization.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "authorization.jar"]