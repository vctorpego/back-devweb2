package com.locadora.videolocadora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // todas as rotas
                        .allowedOrigins("http://localhost:3000", "http://localhost:3001") // Next.js
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos liberados
                        .allowedHeaders("*") // todos os headers
                        .allowCredentials(true); // permite cookies/autenticação
            }
        };
    }
}
