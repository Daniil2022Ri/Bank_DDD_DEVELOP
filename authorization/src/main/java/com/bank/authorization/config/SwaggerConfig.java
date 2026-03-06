package com.bank.authorization.config;

import com.bank.authorization.utils.ApplicationConstants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Value("${swagger.server.description}")
    private String serverDescription;

    @Bean
    public OpenAPI authorizationMicroserviceOpenAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription(serverDescription);

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("Authorization API")
                        .description("API для управления пользователями и аутентификацией")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bank Team")
                                .email("support@bank.com")));
    }
}
