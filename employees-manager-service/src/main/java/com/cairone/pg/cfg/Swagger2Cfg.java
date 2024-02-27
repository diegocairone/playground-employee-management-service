package com.cairone.pg.cfg;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class Swagger2Cfg {

    private final AppScopes appScopes;

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
                                .scopes(appScopes.getScopes())
                        )
                );
    }

    @Value("${app.openapi.idp.authorization-endpoint}")
    private String authorizationUrl;
    @Value("${app.openapi.idp.token-endpoint}")
    private String tokenUrl;


    @Data
    @Component
    @ConfigurationProperties(prefix = "app.openapi.idp")
    public static class AppScopes {

        private Map<String, String> scopesMap;

        public Scopes getScopes() {
            Scopes scopes = new Scopes();
            this.scopesMap.forEach(scopes::addString);
            return scopes;
        }
    }
}
