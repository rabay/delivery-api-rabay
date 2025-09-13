package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test-unit")
@DisplayName("Testes para DbController")
class DbControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("Deve retornar esquema do banco sem parâmetro de tabela")
  void deveRetornarEsquemaBancoSemParametro() throws Exception {
    mockMvc
        .perform(get("/db/schema"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Esquema obtido com sucesso"))
        .andExpect(jsonPath("$.data.tables").isArray())
        .andExpect(jsonPath("$.data.constraints").isArray())
        .andExpect(jsonPath("$.data.keyColumnUsage").isArray())
        .andExpect(jsonPath("$.data.counts").isMap());
  }

  @Test
  @DisplayName("Deve retornar esquema do banco com parâmetro de tabela")
  void deveRetornarEsquemaBancoComParametroTabela() throws Exception {
    mockMvc
        .perform(get("/db/schema").param("table", "CLIENTE"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Esquema obtido com sucesso"))
        .andExpect(jsonPath("$.data.tables").isArray())
        .andExpect(jsonPath("$.data.columns").isArray());
  }

  @Test
  @DisplayName("Deve retornar esquema mesmo com tabela inexistente")
  void deveRetornarEsquemaComTabelaInexistente() throws Exception {
    mockMvc
        .perform(get("/db/schema").param("table", "NON_EXISTENT_TABLE"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.columns").isArray())
        .andExpect(jsonPath("$.data.columns").isEmpty());
  }

  @Test
  @DisplayName("Deve executar teste de integridade do banco")
  void deveExecutarTesteIntegridade() throws Exception {
    mockMvc
        .perform(get("/db/integrity"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Teste de integridade executado"))
        .andExpect(jsonPath("$.data.status").exists());
  }

  @Test
  @DisplayName("Deve executar consulta SELECT válida")
  void deveExecutarConsultaSelectValida() throws Exception {
    String requestBody = "{\"sql\": \"SELECT 1 as test\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Consulta executada com sucesso"))
        .andExpect(jsonPath("$.data.rows").isArray())
        .andExpect(jsonPath("$.data.count").isNumber());
  }

  @Test
  @DisplayName("Deve rejeitar consulta sem campo sql")
  void deveRejeitarConsultaSemCampoSql() throws Exception {
    String requestBody = "{}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Campo 'sql' ausente no corpo da requisição"));
  }

  @Test
  @DisplayName("Deve rejeitar consulta com campo sql vazio")
  void deveRejeitarConsultaComCampoSqlVazio() throws Exception {
    String requestBody = "{\"sql\": \"\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Campo 'sql' ausente no corpo da requisição"));
  }

  @Test
  @DisplayName("Deve rejeitar consulta INSERT")
  void deveRejeitarConsultaInsert() throws Exception {
    String requestBody = "{\"sql\": \"INSERT INTO TESTE VALUES (1)\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Apenas consultas SELECT únicas são permitidas"));
  }

  @Test
  @DisplayName("Deve rejeitar consulta UPDATE")
  void deveRejeitarConsultaUpdate() throws Exception {
    String requestBody = "{\"sql\": \"UPDATE TESTE SET NOME = 'TESTE'\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Apenas consultas SELECT únicas são permitidas"));
  }

  @Test
  @DisplayName("Deve rejeitar consulta DELETE")
  void deveRejeitarConsultaDelete() throws Exception {
    String requestBody = "{\"sql\": \"DELETE FROM TESTE WHERE ID = 1\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Apenas consultas SELECT únicas são permitidas"));
  }

  @Test
  @DisplayName("Deve rejeitar consulta com múltiplas instruções")
  void deveRejeitarConsultaComMultiplasInstrucoes() throws Exception {
    String requestBody = "{\"sql\": \"SELECT 1; SELECT 2\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Apenas consultas SELECT únicas são permitidas"));
  }

  @Test
  @DisplayName("Deve rejeitar consulta muito longa")
  void deveRejeitarConsultaMuitoLonga() throws Exception {
    // Criar uma string maior que 2000 caracteres
    StringBuilder longQuery = new StringBuilder("SELECT ");
    for (int i = 0; i < 200; i++) {
      longQuery.append("1 as col").append(i).append(",");
    }
    longQuery.append("1 as last_col");
    String requestBody = "{\"sql\": \"" + longQuery.toString() + "\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Consulta muito longa"));
  }

  @Test
  @DisplayName("Deve rejeitar consulta que não começa com SELECT")
  void deveRejeitarConsultaQueNaoComecaComSelect() throws Exception {
    String requestBody = "{\"sql\": \"SHOW TABLES\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Apenas consultas SELECT são permitidas"));
  }

  @Test
  @DisplayName("Deve aceitar consulta WITH (CTE)")
  void deveAceitarConsultaWith() throws Exception {
    String requestBody = "{\"sql\": \"WITH CTE AS (SELECT 1 as num) SELECT * FROM CTE\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Consulta executada com sucesso"));
  }

  @Test
  @DisplayName("Deve retornar erro para consulta SQL inválida")
  void deveRetornarErroParaConsultaSqlInvalida() throws Exception {
    String requestBody = "{\"sql\": \"SELECT * FROM TABELA_INEXISTENTE\"}";

    mockMvc
        .perform(post("/db/query").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.data.error").exists());
  }
}
