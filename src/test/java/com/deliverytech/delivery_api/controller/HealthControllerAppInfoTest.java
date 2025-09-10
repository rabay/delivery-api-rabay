package com.deliverytech.delivery_api.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para HealthController.AppInfo")
class HealthControllerAppInfoTest {

  @Test
  @DisplayName("Deve criar AppInfo com todos os campos")
  void deveCriarAppInfoComTodosCampos() {
    // Given
    String application = "Test App";
    String version = "2.0.0";
    String developer = "Test Developer";
    String javaVersion = "JDK 17";
    String framework = "Spring Boot 3.0";

    // When
    HealthController.AppInfo appInfo =
        new HealthController.AppInfo(application, version, developer, javaVersion, framework);

    // Then
    assertThat(appInfo).isNotNull();
    assertThat(appInfo.application()).isEqualTo(application);
    assertThat(appInfo.version()).isEqualTo(version);
    assertThat(appInfo.developer()).isEqualTo(developer);
    assertThat(appInfo.javaVersion()).isEqualTo(javaVersion);
    assertThat(appInfo.framework()).isEqualTo(framework);
  }

  @Test
  @DisplayName("Deve ser igual quando todos os campos são iguais")
  void deveSerIgualQuandoCamposIguais() {
    // Given
    HealthController.AppInfo appInfo1 =
        new HealthController.AppInfo("App", "1.0", "Dev", "JDK 21", "Spring Boot");
    HealthController.AppInfo appInfo2 =
        new HealthController.AppInfo("App", "1.0", "Dev", "JDK 21", "Spring Boot");

    // Then
    assertThat(appInfo1).isEqualTo(appInfo2);
    assertThat(appInfo1.hashCode()).isEqualTo(appInfo2.hashCode());
  }

  @Test
  @DisplayName("Deve ser diferente quando campos são diferentes")
  void deveSerDiferenteQuandoCamposDiferentes() {
    // Given
    HealthController.AppInfo appInfo1 =
        new HealthController.AppInfo("App1", "1.0", "Dev", "JDK 21", "Spring Boot");
    HealthController.AppInfo appInfo2 =
        new HealthController.AppInfo("App2", "1.0", "Dev", "JDK 21", "Spring Boot");

    // Then
    assertThat(appInfo1).isNotEqualTo(appInfo2);
  }

  @Test
  @DisplayName("Deve gerar toString corretamente")
  void deveGerarToStringCorretamente() {
    // Given
    HealthController.AppInfo appInfo =
        new HealthController.AppInfo("TestApp", "1.0", "TestDev", "JDK 21", "Spring Boot");

    // When
    String toString = appInfo.toString();

    // Then
    assertThat(toString).contains("AppInfo");
    assertThat(toString).contains("TestApp");
    assertThat(toString).contains("1.0");
    assertThat(toString).contains("TestDev");
    assertThat(toString).contains("JDK 21");
    assertThat(toString).contains("Spring Boot");
  }

  @Test
  @DisplayName("Deve aceitar valores nulos nos campos")
  void deveAceitarValoresNulos() {
    // When
    HealthController.AppInfo appInfo = new HealthController.AppInfo(null, null, null, null, null);

    // Then
    assertThat(appInfo).isNotNull();
    assertThat(appInfo.application()).isNull();
    assertThat(appInfo.version()).isNull();
    assertThat(appInfo.developer()).isNull();
    assertThat(appInfo.javaVersion()).isNull();
    assertThat(appInfo.framework()).isNull();
  }

  @Test
  @DisplayName("Deve aceitar strings vazias nos campos")
  void deveAceitarStringsVazias() {
    // When
    HealthController.AppInfo appInfo = new HealthController.AppInfo("", "", "", "", "");

    // Then
    assertThat(appInfo).isNotNull();
    assertThat(appInfo.application()).isEmpty();
    assertThat(appInfo.version()).isEmpty();
    assertThat(appInfo.developer()).isEmpty();
    assertThat(appInfo.javaVersion()).isEmpty();
    assertThat(appInfo.framework()).isEmpty();
  }
}
