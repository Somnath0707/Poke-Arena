# Stage 1: Build the JAR with Maven
FROM maven:3.9.9-eclipse-temurin-25-noble AS builder
WORKDIR /build

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the executable jar without running tests inside docker build
RUN mvn clean package -DskipTests

# Stage 2: Lightweight runtime image
FROM eclipse-temurin:25-jre-noble
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Expose server port
EXPOSE 8088

# Launch Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]