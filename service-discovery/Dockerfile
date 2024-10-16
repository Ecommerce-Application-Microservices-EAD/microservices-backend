# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy the project files
COPY . .

# Build the application and print verbose output
RUN mvn clean package -DskipTests -e

# Debugging: List the contents of the target directory
RUN ls -la target

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Debugging: List the contents of the /app directory
RUN ls -la /app

EXPOSE 8761
ENTRYPOINT ["java","-jar","app.jar"]