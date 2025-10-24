# Use Java 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory inside container
WORKDIR /app

# Copy all project files into the container
COPY . /app

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the project using Maven wrapper
RUN ./mvnw package

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the packaged JAR file
CMD ["java", "-jar", "target/ecoplate-0.0.1-SNAPSHOT.jar"]