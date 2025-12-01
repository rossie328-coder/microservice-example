FROM eclipse-temurin:22-jdk-alpine

WORKDIR /app

COPY target/manualentryservice.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]