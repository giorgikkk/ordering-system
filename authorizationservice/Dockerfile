FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} autorization.jar
ENTRYPOINT ["java", "-jar", "/autorization.jar"]
