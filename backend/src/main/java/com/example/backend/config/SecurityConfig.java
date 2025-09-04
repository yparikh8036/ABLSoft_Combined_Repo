package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Basic Spring Security configuration for the application.
 *
 * Current policy:
 * - CORS enabled with defaults (delegates to WebMvc CORS config).
 * - CSRF disabled as the API is stateless and does not rely on cookies.
 * - Stateless sessions suitable for REST APIs.
 * - All requests are permitted (no authentication). Adjust for production.
 */
@Configuration
public class SecurityConfig {
    /**
     * Builds the HTTP security filter chain.
     *
     * @param http Spring Security HttpSecurity to configure
     * @return the built SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }
}
