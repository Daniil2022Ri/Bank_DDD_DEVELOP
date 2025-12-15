package config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI antifraundMicroserviceOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8085/api/antifraund");
        server.setDescription("Local Development Server");

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("Antifraund Microservice API")
                        .description("REST API для управления подозрительными Транзакциями")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bank Development Team")
                                .email("dev@bank.com")));
    }
}