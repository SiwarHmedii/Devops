# Use a Java 17 base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR file from the target directory
COPY target/eventsProject-1.0.0-SNAPSHOT.jar app.jar


# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
