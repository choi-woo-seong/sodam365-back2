FROM openjdk:17-alpine
WORKDIR /app

COPY build/libs/*.jar app.jar
COPY src/main/resources/application.yml ./application.yml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:./application.yml"]
