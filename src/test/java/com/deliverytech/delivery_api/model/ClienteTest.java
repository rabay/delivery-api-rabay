package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ClienteTest {
    @Test
    @DisplayName("Deve criar Cliente com valores padrão corretos")
    void deveCriarClienteComValoresPadrao() {
        Cliente cliente = new Cliente();
        assertThat(cliente.getExcluido()).isFalse();
        assertThat(cliente.isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve permitir set/get de todos os campos")
    void devePermitirSetGetCampos() {
        Cliente cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNome("Teste");
        cliente.setEmail("teste@email.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua Teste, 123");
        cliente.setAtivo(true);
        cliente.setExcluido(true);
        assertThat(cliente.getId()).isEqualTo(10L);
        assertThat(cliente.getNome()).isEqualTo("Teste");
        assertThat(cliente.getEmail()).isEqualTo("teste@email.com");
        assertThat(cliente.getTelefone()).isEqualTo("11999999999");
        assertThat(cliente.getEndereco()).isEqualTo("Rua Teste, 123");
        assertThat(cliente.isAtivo()).isTrue();
        assertThat(cliente.getExcluido()).isTrue();
    }
}
