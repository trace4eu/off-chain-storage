# Stage 1: Build the application
FROM maven:3.9.2-eclipse-temurin-17 as build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
RUN mv target/*.jar app.jar

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
WORKDIR /app
COPY --from=build /app/app.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar", "-i", "config/config.properties", "--server.port=8081"]
