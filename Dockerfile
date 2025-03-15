FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the project files
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Use a lightweight JDK runtime for execution
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built JAR from the first stage
COPY --from=build /app/target/hubby-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
