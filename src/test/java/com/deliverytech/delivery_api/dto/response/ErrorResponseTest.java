package com.deliverytech.delivery_api.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ErrorResponse")
class ErrorResponseTest {

    @Nested
    @DisplayName("Construtor e Getters")
    class ConstructorGettersTests {

        @Test
        @DisplayName("Deve criar ErrorResponse com todos os campos")
        void deveCriarErrorResponseComTodosOsCampos() {
            // Given
            String error = "BadRequest";
            String message = "Requisição inválida";
            int status = 400;
            OffsetDateTime timestamp = OffsetDateTime.now();
            String path = "/api/clientes";
            Map<String, Object> details = Map.of("field", "email", "message", "Email inválido");

            // When
            ErrorResponse errorResponse = new ErrorResponse(error, message, status, timestamp, path, details);

            // Then
            assertThat(errorResponse.error()).isEqualTo(error);
            assertThat(errorResponse.message()).isEqualTo(message);
            assertThat(errorResponse.status()).isEqualTo(status);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo(path);
            assertThat(errorResponse.details()).isEqualTo(details);
        }

        @Test
        @DisplayName("Deve criar ErrorResponse sem detalhes")
        void deveCriarErrorResponseSemDetalhes() {
            // Given
            String error = "NotFound";
            String message = "Recurso não encontrado";
            int status = 404;
            OffsetDateTime timestamp = OffsetDateTime.now();
            String path = "/api/clientes/1";

            // When
            ErrorResponse errorResponse = new ErrorResponse(error, message, status, timestamp, path, null);

            // Then
            assertThat(errorResponse.error()).isEqualTo(error);
            assertThat(errorResponse.message()).isEqualTo(message);
            assertThat(errorResponse.status()).isEqualTo(status);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo(path);
            assertThat(errorResponse.details()).isNull();
        }

        @Test
        @DisplayName("Deve permitir detalhes vazios")
        void devePermitirDetalhesVazios() {
            // Given
            String error = "InternalServerError";
            String message = "Erro interno do servidor";
            int status = 500;
            OffsetDateTime timestamp = OffsetDateTime.now();
            String path = "/api/pedidos";
            Map<String, Object> details = Map.of();

            // When
            ErrorResponse errorResponse = new ErrorResponse(error, message, status, timestamp, path, details);

            // Then
            assertThat(errorResponse.error()).isEqualTo(error);
            assertThat(errorResponse.message()).isEqualTo(message);
            assertThat(errorResponse.status()).isEqualTo(status);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo(path);
            assertThat(errorResponse.details()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Cenários de Erro Comuns")
    class CommonErrorScenariosTests {

        @Test
        @DisplayName("Deve criar erro de requisição inválida")
        void deveCriarErroRequisicaoInvalida() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            Map<String, Object> details = Map.of(
                "field", "email",
                "message", "Email deve ter formato válido"
            );

            // When
            ErrorResponse errorResponse = new ErrorResponse(
                "BadRequest",
                "Requisição inválida",
                400,
                timestamp,
                "/api/clientes",
                details
            );

            // Then
            assertThat(errorResponse.error()).isEqualTo("BadRequest");
            assertThat(errorResponse.message()).isEqualTo("Requisição inválida");
            assertThat(errorResponse.status()).isEqualTo(400);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo("/api/clientes");
            assertThat(errorResponse.details()).containsEntry("field", "email");
            assertThat(errorResponse.details()).containsEntry("message", "Email deve ter formato válido");
        }

        @Test
        @DisplayName("Deve criar erro de recurso não encontrado")
        void deveCriarErroRecursoNaoEncontrado() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();

            // When
            ErrorResponse errorResponse = new ErrorResponse(
                "NotFound",
                "Cliente não encontrado",
                404,
                timestamp,
                "/api/clientes/999",
                null
            );

            // Then
            assertThat(errorResponse.error()).isEqualTo("NotFound");
            assertThat(errorResponse.message()).isEqualTo("Cliente não encontrado");
            assertThat(errorResponse.status()).isEqualTo(404);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo("/api/clientes/999");
            assertThat(errorResponse.details()).isNull();
        }

        @Test
        @DisplayName("Deve criar erro de conflito")
        void deveCriarErroConflito() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            Map<String, Object> details = Map.of(
                "field", "email",
                "message", "Email já cadastrado no sistema"
            );

            // When
            ErrorResponse errorResponse = new ErrorResponse(
                "Conflict",
                "Dados conflitantes",
                409,
                timestamp,
                "/api/clientes",
                details
            );

            // Then
            assertThat(errorResponse.error()).isEqualTo("Conflict");
            assertThat(errorResponse.message()).isEqualTo("Dados conflitantes");
            assertThat(errorResponse.status()).isEqualTo(409);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo("/api/clientes");
            assertThat(errorResponse.details()).containsEntry("field", "email");
            assertThat(errorResponse.details()).containsEntry("message", "Email já cadastrado no sistema");
        }

        @Test
        @DisplayName("Deve criar erro interno do servidor")
        void deveCriarErroInternoServidor() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            Map<String, Object> details = Map.of(
                "traceId", "abc-123-def",
                "operation", "processar_pedido"
            );

            // When
            ErrorResponse errorResponse = new ErrorResponse(
                "InternalServerError",
                "Erro interno do servidor",
                500,
                timestamp,
                "/api/pedidos",
                details
            );

            // Then
            assertThat(errorResponse.error()).isEqualTo("InternalServerError");
            assertThat(errorResponse.message()).isEqualTo("Erro interno do servidor");
            assertThat(errorResponse.status()).isEqualTo(500);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo("/api/pedidos");
            assertThat(errorResponse.details()).containsEntry("traceId", "abc-123-def");
            assertThat(errorResponse.details()).containsEntry("operation", "processar_pedido");
        }

        @Test
        @DisplayName("Deve criar erro de autenticação")
        void deveCriarErroAutenticacao() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();

            // When
            ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized",
                "Credenciais inválidas",
                401,
                timestamp,
                "/api/auth/login",
                null
            );

            // Then
            assertThat(errorResponse.error()).isEqualTo("Unauthorized");
            assertThat(errorResponse.message()).isEqualTo("Credenciais inválidas");
            assertThat(errorResponse.status()).isEqualTo(401);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo("/api/auth/login");
            assertThat(errorResponse.details()).isNull();
        }

        @Test
        @DisplayName("Deve criar erro de autorização")
        void deveCriarErroAutorizacao() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            Map<String, Object> details = Map.of(
                "requiredRole", "ADMIN",
                "userRole", "USER"
            );

            // When
            ErrorResponse errorResponse = new ErrorResponse(
                "Forbidden",
                "Acesso negado",
                403,
                timestamp,
                "/api/admin/clientes",
                details
            );

            // Then
            assertThat(errorResponse.error()).isEqualTo("Forbidden");
            assertThat(errorResponse.message()).isEqualTo("Acesso negado");
            assertThat(errorResponse.status()).isEqualTo(403);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo("/api/admin/clientes");
            assertThat(errorResponse.details()).containsEntry("requiredRole", "ADMIN");
            assertThat(errorResponse.details()).containsEntry("userRole", "USER");
        }
    }

    @Nested
    @DisplayName("Cenários de Validação")
    class ValidationScenariosTests {

        @Test
        @DisplayName("Deve criar erro de validação com múltiplos campos")
        void deveCriarErroValidacaoMultiplosCampos() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            Map<String, Object> details = Map.of(
                "nome", "Nome é obrigatório",
                "email", "Email deve ter formato válido",
                "telefone", "Telefone deve ter 11 dígitos"
            );

            // When
            ErrorResponse errorResponse = new ErrorResponse(
                "ValidationError",
                "Dados de entrada inválidos",
                422,
                timestamp,
                "/api/clientes",
                details
            );

            // Then
            assertThat(errorResponse.error()).isEqualTo("ValidationError");
            assertThat(errorResponse.message()).isEqualTo("Dados de entrada inválidos");
            assertThat(errorResponse.status()).isEqualTo(422);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo("/api/clientes");
            assertThat(errorResponse.details()).hasSize(3);
            assertThat(errorResponse.details()).containsEntry("nome", "Nome é obrigatório");
            assertThat(errorResponse.details()).containsEntry("email", "Email deve ter formato válido");
            assertThat(errorResponse.details()).containsEntry("telefone", "Telefone deve ter 11 dígitos");
        }

        @Test
        @DisplayName("Deve criar erro de validação com detalhes estruturados")
        void deveCriarErroValidacaoDetalhesEstruturados() {
            // Given
            OffsetDateTime timestamp = OffsetDateTime.now();
            Map<String, Object> details = Map.of(
                "errors", Map.of(
                    "nome", "Nome é obrigatório",
                    "endereco", Map.of(
                        "rua", "Rua é obrigatória",
                        "numero", "Número deve ser positivo"
                    )
                ),
                "totalErrors", 3
            );

            // When
            ErrorResponse errorResponse = new ErrorResponse(
                "ValidationError",
                "Erros de validação encontrados",
                422,
                timestamp,
                "/api/clientes",
                details
            );

            // Then
            assertThat(errorResponse.error()).isEqualTo("ValidationError");
            assertThat(errorResponse.message()).isEqualTo("Erros de validação encontrados");
            assertThat(errorResponse.status()).isEqualTo(422);
            assertThat(errorResponse.timestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.path()).isEqualTo("/api/clientes");
            assertThat(errorResponse.details()).containsKey("errors");
            assertThat(errorResponse.details()).containsEntry("totalErrors", 3);
        }
    }
}
