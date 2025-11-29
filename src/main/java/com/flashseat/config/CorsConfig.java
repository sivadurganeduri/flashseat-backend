package com.flashseat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // All endpoints
                .allowedOrigins("http://localhost:5173", "http://localhost:3000", "https://flashseat.vercel.app")  // Frontend origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // All methods
                .allowedHeaders("*")  // All headers (including Authorization)
                .allowCredentials(true);  // Cookies/JWT
    }
}