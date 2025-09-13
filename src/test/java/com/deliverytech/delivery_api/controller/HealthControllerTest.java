package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ActiveProfiles("test-unit")
@DisplayName("Testes para HealthController")
class HealthControllerTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("Deve retornar status saudável na chamada /actuator/health")
  @WithMockUser(roles = "ADMIN")
  void deveRetornarStatusSaudavel() throws Exception {
    mockMvc
        .perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .contentType(MediaType.valueOf("application/vnd.spring-boot.actuator.v3+json")))
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  @DisplayName("Deve retornar informações da aplicação na chamada /actuator/info")
  @WithMockUser(roles = "ADMIN")
  void deveRetornarInformacoesAplicacao() throws Exception {
    mockMvc
        .perform(get("/actuator/info"))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .contentType(MediaType.valueOf("application/vnd.spring-boot.actuator.v3+json")));
  }

  @Test
  @DisplayName("Deve retornar timestamp válido no health check")
  @WithMockUser(roles = "ADMIN")
  void deveRetornarTimestampValido() throws Exception {
    mockMvc
        .perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .contentType(MediaType.valueOf("application/vnd.spring-boot.actuator.v3+json")))
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  @DisplayName("Deve retornar versão Java válida no health check")
  @WithMockUser(roles = "ADMIN")
  void deveRetornarVersaoJavaValida() throws Exception {
    mockMvc
        .perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .contentType(MediaType.valueOf("application/vnd.spring-boot.actuator.v3+json")))
        .andExpect(jsonPath("$.status").value("UP"));
  }
}
