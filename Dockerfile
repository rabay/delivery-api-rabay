# Dockerfile multi-stage para aplicação Spring Boot
# Estágio 1: Build
FROM openjdk:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Estágio 2: Runtime
FROM openjdk:21-slim AS runtime
WORKDIR /app
# Cria usuário e grupo não-root
RUN groupadd -r appuser && useradd -r -g appuser appuser
# Copia apenas o JAR gerado do estágio de build
COPY --from=build /app/target/delivery-api-0.0.1-SNAPSHOT.jar app.jar
# Ajusta permissões do diretório e do JAR
RUN chown -R appuser:appuser /app
EXPOSE 8080
# Healthcheck para endpoint padrão do Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
	CMD curl -f http://localhost:8080/health || exit 1
# Troca para usuário não-root
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
