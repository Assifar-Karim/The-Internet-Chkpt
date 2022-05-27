# Build Stage
FROM openjdk:11-jdk-slim AS build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw compile
RUN ./mvnw package -Dmaven.test.skip

# Execution Stage
FROM openjdk:11-jre

VOLUME /tmp

RUN mkdir ticp
WORKDIR /ticp

COPY --from=build /workspace/app/target/the-internet-checkpoint-1.0.0.jar ./target/the-internet-checkpoint-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./target/the-internet-checkpoint-1.0.0.jar"]