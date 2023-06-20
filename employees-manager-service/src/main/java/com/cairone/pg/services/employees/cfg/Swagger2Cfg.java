package com.cairone.pg.services.employees.cfg;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger2Cfg implements OpenApiCustomiser {

    @Override
    public void customise(OpenAPI openApi) {
        openApi.getInfo()
                .title("Employees Manager service")
                .description(
                        "The Employees Manager service is a Spring Boot-based RESTful API developed for " +
                        "educational purposes. This application serves as a demonstration of how to create a " +
                        "simple system for managing employee information using the Spring Boot framework");
    }
}
