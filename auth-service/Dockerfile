# ─── Naik Org — Auth Service Dockerfile ─────────────────────────────────────
# Step 1: Build JAR locally → mvn clean package -DskipTests
# Step 2: Build image      → docker build -t naik-auth:latest .

FROM --platform=linux/amd64 eclipse-temurin:25-jre-alpine

# Metadata
LABEL maintainer="Naik Org"
LABEL service="naik-auth"
LABEL version="1.0.0"

# Create non-root user for security
RUN addgroup -S naik && adduser -S naik -G naik

WORKDIR /app

# Copy built JAR — requires mvn clean package -DskipTests first
COPY target/*.jar app.jar

RUN chown naik:naik app.jar

USER naik

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:8080/auth/api/health || exit 1

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
