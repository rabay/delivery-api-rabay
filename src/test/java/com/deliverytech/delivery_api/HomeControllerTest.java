package com.deliverytech.delivery_api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BuildProperties buildProperties;

    @Test
    @DisplayName("GET / deve retornar HTML com links e content-type text/html")
    void home_returnsHtml() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("/actuator/health")))
                .andExpect(content().string(containsString("/info")))
                .andExpect(content().string(containsString("/java-features/pattern-matching/150")))
                .andExpect(content().string(containsString("/java-features/virtual-threads")));
    }

    @Test
    @DisplayName("GET /info retorna AppInfo com dados do BuildProperties")
    void info_returnsAppInfo() throws Exception {
        given(buildProperties.getName()).willReturn("delivery-api");
        given(buildProperties.getVersion()).willReturn("1.2.3");
        given(buildProperties.getGroup()).willReturn("com.deliverytech");
        given(buildProperties.getArtifact()).willReturn("delivery-api");

        mvc.perform(get("/info")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.application").value("delivery-api"))
                .andExpect(jsonPath("$.version").value("1.2.3"))
                .andExpect(jsonPath("$.developer").value("com.deliverytech:delivery-api"))
                .andExpect(jsonPath("$.javaVersion").exists())
                .andExpect(jsonPath("$.framework").exists());
    }
}
