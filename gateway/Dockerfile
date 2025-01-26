# Use an official Maven image to build the project
FROM maven:3.8.5-openjdk-11-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and the source code into the container
COPY pom.xml /app
COPY src /app/src

# Build the project and create the JAR file
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar /app/gateway.jar

# Make port 8090 available to the world outside this container
EXPOSE 8090

# Run the application
CMD ["java", "-jar", "gateway.jar"]