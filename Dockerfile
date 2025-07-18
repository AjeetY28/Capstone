# Use the official OpenJDK 17 image as a base
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests



# Add a volume to store logs
FROM openjdk:17-jdk-slim
WORKDIR /app
VOLUME /tmp

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
# # Copy the JAR file to the container
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar

# # Expose port 8080 (or your configured port)
# EXPOSE 8080

# # Run the Spring Boot app
# ENTRYPOINT ["java", "-jar", "app.jar"]
