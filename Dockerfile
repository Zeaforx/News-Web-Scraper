# Stage 1: Build the Grails application
FROM openjdk:17-jdk-slim-buster AS build

WORKDIR /app

# Copy the entire project context into the /app directory in the build stage.
# This ensures all necessary source files and build scripts are available.
# A .dockerignore file can be used to exclude unnecessary files.
COPY . . 

# Make gradlew executable
RUN chmod +x ./gradlew

# Run a dummy build to download dependencies and cache them
RUN ./gradlew dependencies

# Build the Grails application
RUN ./gradlew bootJar

# Stage 2: Create the final lean runtime image
FROM openjdk:17-slim-buster

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/myapp-0.1.jar application.jar

# Expose the port your Grails application listens on (default is 8080)
EXPOSE 8080

# Set environment variables for JVM options
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Xmx512m -Xms256m"

# Command to run the application when the container starts
CMD ["java", "-jar", "application.jar"]