# Use a base image with JDK 17 (or your Java version)
FROM openjdk:17-jdk-alpine

# Set a working directory in the container
WORKDIR /app

# Copy the built jar file into the container
COPY build/libs/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]

