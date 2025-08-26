# Stage 1: Build with Gradle
FROM gradle:8.14.3-jdk24 AS build
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon -x test


# Stage 2: Run with Java
FROM eclipse-temurin:24-jdk AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]