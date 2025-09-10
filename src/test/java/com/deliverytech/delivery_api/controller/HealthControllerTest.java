package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test-unit")
@DisplayName("Testes para HealthController")
class HealthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("Deve retornar status saudável na chamada /health")
  void deveRetornarStatusSaudavel() throws Exception {
    mockMvc
        .perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Aplicação saudável"))
        .andExpect(jsonPath("$.data.status").value("UP"))
        .andExpect(jsonPath("$.data.service").value("Delivery API"))
        .andExpect(jsonPath("$.data.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.data.javaVersion").isNotEmpty());
  }

  @Test
  @DisplayName("Deve retornar informações da aplicação na chamada /info")
  void deveRetornarInformacoesAplicacao() throws Exception {
    mockMvc
        .perform(get("/info"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Informações da aplicação"))
        .andExpect(jsonPath("$.data.application").value("Delivery Tech API"))
        .andExpect(jsonPath("$.data.version").value("1.0.0"))
        .andExpect(jsonPath("$.data.developer").value("Victor Alexandre Rabay"))
        .andExpect(jsonPath("$.data.javaVersion").value("JDK 21"))
        .andExpect(jsonPath("$.data.framework").value("Spring Boot 3.2.x"));
  }

  @Test
  @DisplayName("Deve retornar timestamp válido no health check")
  void deveRetornarTimestampValido() throws Exception {
    mockMvc
        .perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Aplicação saudável"))
        .andExpect(jsonPath("$.data.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.data.timestamp").isString());
  }

  @Test
  @DisplayName("Deve retornar versão Java válida no health check")
  void deveRetornarVersaoJavaValida() throws Exception {
    mockMvc
        .perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Aplicação saudável"))
        .andExpect(jsonPath("$.data.javaVersion").isNotEmpty())
        .andExpect(jsonPath("$.data.javaVersion").isString());
  }
}
