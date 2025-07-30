# Use a slim OpenJDK image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container
COPY target/task_1-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# COPY .ENV
COPY .env src/main/resources/.env


# Expose any port your app uses (optional, only if needed)
# EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
