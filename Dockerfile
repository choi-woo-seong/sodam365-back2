# OpenJDK 17 기반 스프링부트이미지
FROM openjdk:17-alpine
# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일을 컨테이너에 복사
COPY build/libs/*.jar app.jar
COPY src/main/resources/application.yml application.yml

# 포트 설정
EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]