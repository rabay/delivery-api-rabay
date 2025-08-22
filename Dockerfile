
# Dockerfile multi-stage otimizado para aplicação Spring Boot

# ----------- BUILDER STAGE -----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia arquivos essenciais para dependências (melhor cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código
COPY src src

# Compila o projeto (gera o jar na pasta target)
RUN mvn clean package -DskipTests

# ----------- RUNTIME STAGE -----------
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app


# Cria usuário não-root para rodar a aplicação
RUN useradd -m spring && mkdir /app/logs && chown -R spring:spring /app

# Copia apenas o jar gerado do builder
COPY --from=builder /app/target/delivery-api-0.0.1-SNAPSHOT.jar app.jar

# Permissões restritivas (antes de trocar usuário)
RUN chown spring:spring /app/app.jar && chmod 500 /app/app.jar

# Troca para usuário não-root após permissionamento
USER spring

# Variáveis de ambiente recomendadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Expondo porta padrão Spring Boot
EXPOSE 8080

# Healthcheck para endpoint /health da aplicação
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
	CMD curl -f http://localhost:8080/health || exit 1

# Entrypoint seguro
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
