FROM openjdk:17-alpine
WORKDIR /app

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 환경 변수 설정 (JWT 관련)
ENV JWT_SECRET=mysupersecretkeymysupersecretkeymysupersecretkey
ENV JWT_EXPIRATION=3600000

# 포트 오픈
EXPOSE 8080

# 실행 시 application.yml 없이 환경 변수로 설정
ENTRYPOINT ["java", "-jar", "app.jar"]
