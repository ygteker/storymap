FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy Quarkus application output
COPY build/quarkus-app/lib/ /app/lib/
COPY build/quarkus-app/*.jar /app/
COPY build/quarkus-app/app/ /app/app/
COPY build/quarkus-app/quarkus/ /app/quarkus/

# Expose default Quarkus port
EXPOSE 8080

# Run the Quarkus application
CMD ["java", "-jar", "/app/quarkus-run.jar"]