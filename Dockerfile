FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/payroll-0.0.1-SNAPSHOT.jar payroll.jar

ENTRYPOINT ["java", "-jar", "payroll.jar"]

LABEL authors="HP"