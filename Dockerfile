
# ----------- BUILDER STAGE -----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia apenas arquivos essenciais para cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia código fonte e configs
COPY src src
COPY config config

# Compila o projeto (gera o jar na pasta target) - testes já rodaram fora do docker build
RUN mvn clean package -DskipTests


# ----------- RUNTIME STAGE -----------
# Para máxima segurança, pode-se usar distroless. Aqui mantemos eclipse-temurin por flexibilidade.
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# Cria usuário não-root e diretório de logs
RUN useradd -r -d /app -s /usr/sbin/nologin spring \
		&& mkdir -p /app/logs \
		&& chown -R spring:spring /app

# Copia apenas o JAR final
COPY --from=builder /app/target/delivery-api-0.0.1-SNAPSHOT.jar app.jar

# Permissões restritivas
RUN chmod 500 /app/app.jar && chown spring:spring /app/app.jar

# Troca para usuário não-root
USER spring

# Variáveis de ambiente recomendadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Expondo porta padrão Spring Boot
EXPOSE 8080

# Healthcheck para endpoint /health
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
	CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Entrypoint seguro
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]