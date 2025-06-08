FROM openjdk:17-jdk-alpine
#TODO: change before pushing
COPY build/libs/app.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
