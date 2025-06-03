# Stage 1: Build the Grails application
FROM openjdk:17-jdk-slim-buster AS build

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code (optional: you can copy it later to leverage Docker cache more effectively if dependencies don't change)
COPY src src

# Make gradlew executable
RUN chmod +x ./gradlew

# Run a dummy build to download dependencies and cache them
# This helps speed up subsequent builds if dependencies don't change
RUN ./gradlew dependencies

# Build the Grails application
RUN ./gradlew bootJar

# Stage 2: Create the final lean runtime image
FROM openjdk:17-jre-slim-buster

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/myapp-0.1.jar application.jar

# Expose the port your Grails application listens on (default is 8080)
EXPOSE 8080

# Set environment variables for JVM options
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Xmx512m -Xms256m"

# Command to run the application when the container starts
CMD ["java", "-jar", "application.jar"]