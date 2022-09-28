FROM openjdk:20-slim-buster
WORKDIR /app
ADD build/libs/students-info-service-0.0.1-SNAPSHOT.jar /app/students-info-service.jar
ENTRYPOINT ["java","-jar","/app/students-info-service.jar"]