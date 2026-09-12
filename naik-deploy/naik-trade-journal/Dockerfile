# ─── Naik Org — Trade Journal Dockerfile ────────────────────────────────────
# Build locally first: mvn clean package -DskipTests
# Then build this image: docker build -t naik-trade-journal .

FROM eclipse-temurin:17-jre-alpine

# Metadata
LABEL maintainer="Naik Org"
LABEL service="naik-trade-journal"
LABEL version="1.0.0"

# Create non-root user for security
RUN addgroup -S naik && adduser -S naik -G naik

# Set working directory
WORKDIR /app

# Copy the built JAR from target/
# Run: mvn clean package -DskipTests before building this image
COPY target/*.jar app.jar

# Change ownership to non-root user
RUN chown naik:naik app.jar

# Switch to non-root user
USER naik

# Expose port
EXPOSE 1111

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:1111/journal/api/trades/health || exit 1

# Run the JAR
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
