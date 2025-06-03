# Use a lightweight OpenJDK base image for running the JAR
FROM openjdk:17-slim-buster

# Set the working directory inside the container
WORKDIR /app

# Copy the Grails bootJar into the container
# REPLACE 'your-grails-app-name-0.1.jar' with the actual filename from your build/libs folder
COPY build/libs/myapp-0.1.jar application.jar

# Expose the port your Grails application listens on (default is 8080)
EXPOSE 8080

# Set environment variables for JVM options (optional, but good practice for memory management)
# Adjust Xmx (max heap) and Xms (initial heap) based on your application's needs
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Xmx512m -Xms256m"

# Command to run the application when the container starts
CMD ["java", "-jar", "application.jar"]