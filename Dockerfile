# Use Eclipse Temurin OpenJDK 21 (stable for Render)
FROM eclipse-temurin:21-jdk

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