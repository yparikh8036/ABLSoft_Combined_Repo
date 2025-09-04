package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the application.
 *
 * This setup is permissive and intended for development/demo purposes:
 * - Allows requests from any origin, with any method and headers.
 * - Credentials are disabled; set allowCredentials(true) only when restricting origins explicitly.
 * - Max age defines how long preflight results can be cached by the client (in seconds).
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Registers global CORS mappings for all endpoints.
     *
     * @param registry the CORS registry used to add mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")     // allow all origins
            .allowedMethods("*")     // allow all HTTP methods
            .allowedHeaders("*")     // allow all headers
            .allowCredentials(false) // set to true only if you list explicit origins
            .maxAge(3600);
    }
}
