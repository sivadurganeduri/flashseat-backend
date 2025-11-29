# Use OpenJDK 21 (your Java version)
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build JAR with Maven
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Run JAR
CMD ["java", "-jar", "target/flashseat-backend-0.0.1-SNAPSHOT.jar"]