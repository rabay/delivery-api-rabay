package com.deliverytech.delivery_api;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final BuildProperties buildProperties;

    public HomeController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
            <h1>Delivery API</h1>
            <h2>📋 Endpoints Disponíveis:</h2>
            <ul>
                <li><a href='/actuator/health'>/actuator/health</a> - Status da aplicação</li>
                <li><a href='/info'>/info</a> - Informações do projeto</li>
                <li><a href='/java-features/pattern-matching/150'>/java-features/pattern-matching/{input}</a> - Pattern Matching (Java 17+)</li>
                <li><a href='/java-features/virtual-threads'>/java-features/virtual-threads</a> - Virtual Threads (Java 21)</li>
            </ul>
            """;
    }

    @GetMapping("/info")
    public AppInfo info() {
        return new AppInfo(
            buildProperties.getName(),
            buildProperties.getVersion(),
            buildProperties.getGroup() + ":" + buildProperties.getArtifact(),
            System.getProperty("java.version"),
            "Spring Boot " + org.springframework.boot.SpringBootVersion.getVersion()
        );
    }

    // Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
    public record AppInfo(
        String application,
        String version,
        String developer,
        String javaVersion,
        String framework
    ) {}
}
