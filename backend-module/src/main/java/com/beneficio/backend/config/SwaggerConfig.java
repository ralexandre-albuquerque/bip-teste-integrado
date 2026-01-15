package com.beneficio.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Benefícios")
                        .version("v1")
                        .description("API para gerenciamento de benefícios corporativos"))
                .components(new Components()
                        // Resposta 400 - Bad Request (Validação)
                        .addResponses("bad_request", new ApiResponse()
                                .description("Dados de entrada inválidos")
                                .content(new Content()
                                        .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .$ref("#/components/schemas/ErrorResponse")))))

                        // Resposta 500 - Internal Server Error
                        .addResponses("internal_error", new ApiResponse()
                                .description("Erro interno do servidor")
                                .content(new Content()
                                        .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .$ref("#/components/schemas/ErrorResponse")))))
                );
    }
}