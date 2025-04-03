FROM openjdk:17-alpine
WORKDIR /app

COPY build/libs/*.jar app.jar
COPY src/main/resources/application.yml ./application.yml

EXPOSE 8080

ENV JWT_SECRET=mysupersecretkeymysupersecretkeymysupersecretkey
ENV JWT_EXPIRATION=3600000

ENTRYPOINT ["sh", "-c", "java -Djwt.secret=$JWT_SECRET -Djwt.expiration=$JWT_EXPIRATION -jar app.jar --spring.config.location=file:./application.yml"]
