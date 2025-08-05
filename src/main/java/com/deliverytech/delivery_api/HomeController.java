package main.java.com.deliverytech.delivery_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return """
            <h1>Delivery API</h1>
            <h2>📋 Endpoints Disponíveis:</h2>
            <ul>
                <li><a href='/health'>/health</a> - Status da aplicação</li>
                <li><a href='/info'>/info</a> - Informações do projeto</li>
                <li><a href='/java-features/pattern-matching/150'>/java-features/pattern-matching/{input}</a> - Pattern Matching (Java 17+)</li>
                <li><a href='/java-features/virtual-threads'>/java-features/virtual-threads</a> - Virtual Threads (Java 21)</li>
            </ul>
            """;
    }

    @GetMapping("/info")
    public AppInfo info() {
        return new AppInfo(
            "Delivery Tech API",
            "1.0.0",
            "Victor Rabay",
            "JDK 21",
            "Spring Boot 3.5.4"
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
