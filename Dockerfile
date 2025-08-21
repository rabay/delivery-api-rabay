# Dockerfile multi-stage para aplicação Spring Boot
# Estágio 1: Build
FROM openjdk:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Estágio 2: Runtime
FROM openjdk:21-slim AS runtime
WORKDIR /app
# Copia apenas o JAR gerado do estágio de build
COPY --from=build /app/target/delivery-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
