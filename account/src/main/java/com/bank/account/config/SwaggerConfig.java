package com.bank.account.config;

import com.bank.account.util.ApplicationConstants;
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

    private final ApplicationConstants constants;

    public SwaggerConfig(ApplicationConstants constants) {
        this.constants = constants;
    }

    @Bean
    public OpenAPI accountMicroserviceOpenAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription(serverDescription);

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title(constants.SWAGGER_TITLE)
                        .description(constants.SWAGGER_DESCRIPTION)
                        .version(constants.SWAGGER_VERSION)
                        .contact(new Contact()
                                .name(constants.SWAGGER_CONTACT_NAME)
                                .email(constants.SWAGGER_CONTACT_EMAIL)));
    }
}


