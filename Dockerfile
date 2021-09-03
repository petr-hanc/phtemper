FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} phtemper.jar
COPY db db
CMD ["java","-jar","/phtemper.jar"]