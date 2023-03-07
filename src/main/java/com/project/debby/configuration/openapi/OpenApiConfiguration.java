package com.project.debby.configuration.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenApi(@Value("Debby - personal loan tracker.")String appDescription,
                                 @Value("0.0.1")String appVersion) {
        return new OpenAPI().info(new Info().title("Debby service API")
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name("not licensed yet")
                                .url("http://localhost/"))
                        .contact(new Contact().name("Daniil \"Seekerses\" Popov")
                                .email("seekergodlike@gmail.com")))
                .servers(List.of(new Server().url("http://87.239.105.85:8080")
                        .description("Main server")));
    }
}
