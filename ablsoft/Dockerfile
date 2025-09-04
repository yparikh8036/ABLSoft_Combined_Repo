# syntax=docker/dockerfile:1

# ---------- Build stage ----------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /workspace

# Copy Gradle wrapper and build files first for better caching
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle ./gradle

# Download dependencies (will be cached if files unchanged)
RUN ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

# Copy source code
COPY src ./src

# Build the Spring Boot fat jar
RUN ./gradlew --no-daemon clean bootJar

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-alpine AS runtime

# Add non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copy boot jar
COPY --from=build /workspace/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# JVM options can be overridden at runtime with JAVA_OPTS env
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -XX:+ExitOnOutOfMemoryError"

# Spring profile can be passed via SPRING_PROFILES_ACTIVE
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]