package com.pokearena.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // 1. Configure JWT Bearer Authentication for Swagger UI
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.HTTP);
        securityScheme.setScheme("bearer");
        securityScheme.setBearerFormat("JWT");
        securityScheme.setDescription("Enter your JWT token here to access protected endpoints.");

        Components components = new Components();
        components.addSecuritySchemes("BearerAuth", securityScheme);

        // 2. Add security requirement so the "Authorize" lock appears
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("BearerAuth");

        // 3. Basic API documentation details
        Info info = new Info();
        info.setTitle("Poke-Arena REST API");
        info.setVersion("1.0.0");
        info.setDescription("API documentation for Poke-Arena combat simulation, team management, and leaderboards.");

        // 4. Assemble OpenAPI configuration
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        openAPI.setComponents(components);
        openAPI.addSecurityItem(securityRequirement);

        return openAPI;
    }
}
