package com.deliverytech.delivery_api.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para ApiResponse")
class ApiResponseTest {

    @Test
    @DisplayName("Deve criar ApiResponse com dados")
    void deveCriarApiResponseComDados() {
        // Given
        String data = "Teste";
        String message = "Operação realizada com sucesso";
        boolean success = true;

        // When
        ApiResponse<String> response = new ApiResponse<>(data, message, success);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.data()).isEqualTo(data);
        assertThat(response.message()).isEqualTo(message);
        assertThat(response.success()).isTrue();
    }

    @Test
    @DisplayName("Deve criar ApiResponse sem dados")
    void deveCriarApiResponseSemDados() {
        // When
        ApiResponse<Void> response = new ApiResponse<>(null, "Erro interno", false);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("Erro interno");
        assertThat(response.success()).isFalse();
    }

    @Test
    @DisplayName("Deve criar ApiResponse com objeto complexo")
    void deveCriarApiResponseComObjetoComplexo() {
        // Given
        TestData testData = new TestData("João", 25);

        // When
        ApiResponse<TestData> response = new ApiResponse<>(testData, "Dados retornados", true);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.data()).isEqualTo(testData);
        assertThat(response.data().nome()).isEqualTo("João");
        assertThat(response.data().idade()).isEqualTo(25);
        assertThat(response.message()).isEqualTo("Dados retornados");
        assertThat(response.success()).isTrue();
    }

    @Test
    @DisplayName("Deve ser igual quando todos os campos são iguais")
    void deveSerIgualQuandoTodosCamposSaoIguais() {
        // Given
        ApiResponse<String> response1 = new ApiResponse<>("teste", "ok", true);
        ApiResponse<String> response2 = new ApiResponse<>("teste", "ok", true);

        // Then
        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("Deve ser diferente quando campos são diferentes")
    void deveSerDiferenteQuandoCamposSaoDiferentes() {
        // Given
        ApiResponse<String> response1 = new ApiResponse<>("teste1", "ok", true);
        ApiResponse<String> response2 = new ApiResponse<>("teste2", "ok", true);

        // Then
        assertThat(response1).isNotEqualTo(response2);
    }

    @Test
    @DisplayName("Deve gerar toString corretamente")
    void deveGerarToStringCorretamente() {
        // Given
        ApiResponse<String> response = new ApiResponse<>("dados", "mensagem", true);

        // When
        String toString = response.toString();

        // Then
        assertThat(toString).contains("ApiResponse");
        assertThat(toString).contains("dados");
        assertThat(toString).contains("mensagem");
        assertThat(toString).contains("true");
    }

    // Classe auxiliar para teste
    private record TestData(String nome, int idade) {}
}
