# Stage 1: Build
FROM amazoncorretto:17 as builder

WORKDIR /app
COPY . .

RUN ./gradlew clean build -x test
RUN ls -la /app/build/libs/

# Stage 2: Run
FROM amazoncorretto:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar
COPY src/main/resources/application.yml /app/application.yml 

EXPOSE 8080
ENV TZ Asia/Seoul

# ✅ 변경된 entrypoint (Vault 제거)
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.config.location=file:/app/application.yml"]
