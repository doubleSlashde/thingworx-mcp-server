# syntax=docker/dockerfile:1.7

# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Leverage Docker layer caching for dependencies
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -B -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -B -DskipTests package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre AS runtime

# Create a non-root user
RUN useradd -u 10001 -m appuser
WORKDIR /app

# Copy fat jar from build stage
# The Spring Boot repackage goal creates a single runnable jar in target/
COPY --from=build /workspace/target/*-SNAPSHOT.jar /app/app.jar

# Set container-friendly JVM defaults
ENV JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=50.0 -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:+UseStringDeduplication"

# Default Spring Boot port can be overridden at runtime by SERVER_PORT env var
ENV SERVER_PORT=8081
EXPOSE 8081

USER appuser

ENTRYPOINT ["java","-jar","/app/app.jar"]
