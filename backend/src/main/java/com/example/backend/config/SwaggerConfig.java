package com.example.backend.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Swagger/OpenAPI configuration for API documentation.
 *
 * Exposes the OpenAPI bean consumed by springdoc-openapi to render Swagger UI.
 */
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    /**
     * Defines the OpenAPI specification for the application.
     *
     * @return OpenAPI instance with title, description, and version
     */
    @Bean
    public OpenAPI invoiceApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Invoice CSV Upload API")
                        .description("API for uploading and processing CSV invoice files")
                        .version("1.0.0"));
    }
}
