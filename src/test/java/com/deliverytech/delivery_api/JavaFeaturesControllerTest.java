package com.deliverytech.delivery_api;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

@WebMvcTest(controllers = JavaFeaturesController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
class JavaFeaturesControllerTest {

    @Autowired
    MockMvc mvc;

    @TestConfiguration
    static class TestBeansConfig {
        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @Test
    @DisplayName("pattern-matching: numero grande")
    void patternMatching_largeNumber() throws Exception {
        mvc.perform(get("/java-features/pattern-matching/150"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.parsedType").value("Integer"))
                .andExpect(jsonPath("$.result", containsString("Número grande")));
    }

    @Test
    @DisplayName("pattern-matching: numero pequeno")
    void patternMatching_smallNumber() throws Exception {
        mvc.perform(get("/java-features/pattern-matching/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parsedType").value("Integer"))
                .andExpect(jsonPath("$.result", containsString("Número pequeno")));
    }

    @Test
    @DisplayName("pattern-matching: zero ou negativo")
    void patternMatching_zeroNegative() throws Exception {
        mvc.perform(get("/java-features/pattern-matching/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", containsString("zero ou negativo")));
    }

    @Test
    @DisplayName("pattern-matching: string longa e curta")
    void patternMatching_strings() throws Exception {
        mvc.perform(get("/java-features/pattern-matching/abcdefghijk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parsedType").value("String"))
                .andExpect(jsonPath("$.result", containsString("String longa")));

        mvc.perform(get("/java-features/pattern-matching/abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", containsString("String curta")));
    }

    @Test
    @DisplayName("virtual-threads: deve retornar dto com campos esperados")
    void virtualThreads_returnsDto() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/java-features/virtual-threads"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.feature").value("Virtual Threads (Java 21)"))
                .andExpect(jsonPath("$.tasks").isMap())
                .andExpect(jsonPath("$.threadType", anyOf(is("Virtual"), is("Platform"))))
                .andExpect(jsonPath("$.executionTime", containsString("ms")));
    }
}
