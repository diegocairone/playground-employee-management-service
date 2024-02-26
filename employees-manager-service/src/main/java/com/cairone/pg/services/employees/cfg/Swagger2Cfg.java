package com.cairone.pg.services.employees.cfg;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class Swagger2Cfg {

    @Bean
    public OpenAPI getOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("http", getSecuritySchemeHttpType())
                        .addSecuritySchemes("oauth", getSecuritySchemeOauth2Type())
                )
                .security(Arrays.asList(
                        new SecurityRequirement().addList("http"),
                        new SecurityRequirement().addList("oauth")))
                .info(new Info()
                        .title("Employees Manager service")
                        .description(
                                "The Employees Manager service is a Spring Boot-based RESTful API developed for " +
                                        "educational purposes. This application serves as a demonstration of how to create a " +
                                        "simple system for managing employee information using the Spring Boot framework")
                );
    }

    public SecurityScheme getSecuritySchemeHttpType() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

    private SecurityScheme getSecuritySchemeOauth2Type() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("Keycloak")
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(authorizationUrl)
                                .tokenUrl(tokenUrl)
                                .scopes(new Scopes()
                                        .addString("profile", "profile")
                                        .addString("email", "email"))
                        )
                );
    }

    @Value("${spring.security.oauth2.resource-server.jwt.issuer-uri}/protocol/openid-connect/auth")
    private String authorizationUrl;
    @Value("${spring.security.oauth2.resource-server.jwt.issuer-uri}/protocol/openid-connect/token")
    private String tokenUrl;
}
