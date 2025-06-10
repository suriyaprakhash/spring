# Stage 1: Build stage
# FROM eclipse-temurin:21.0.4_7-jdk-ubi9-minimal AS builder
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu AS builder

WORKDIR /app
COPY . /app

RUN pwd
RUN ls -alh

# RUN apk add --no-cache maven
# RUN mvn clean package

RUN chmod +x mvnw
RUN ./mvnw clean package

# Stage 2: Runtime stage
# FROM eclipse-temurin:21.0.4_7-jre-ubi9-minimal
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

# Create a user to run as a non root
RUN useradd --create-home appuser
WORKDIR /home/appuser/app
USER appuser

# Copy the jar from the builder
COPY --from=builder /app/target/*.jar /home/appuser/app/cache-web.jar

EXPOSE 8080

CMD ["java", "-jar", "cache-web.jar"]