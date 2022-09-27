# Build Stage
FROM openjdk:11-jdk-slim AS build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

# Execution Stage
FROM openjdk:11-jre

RUN mkdir ticp
WORKDIR /ticp

VOLUME /ticp/The-Internet-Chkpt

COPY --from=build /workspace/app/target/the-internet-checkpoint-1.0.0.jar ./target/the-internet-checkpoint-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./target/the-internet-checkpoint-1.0.0.jar"]