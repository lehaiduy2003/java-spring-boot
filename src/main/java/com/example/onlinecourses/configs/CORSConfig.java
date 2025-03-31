package com.example.onlinecourses.configs;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

    private static final String[] ALLOWED_METHODS = { "GET", "POST", "PUT", "DELETE", "PATCH" };
    private static final String[] ALLOWED_HEADERS = { "Authorization", "Content-Type", "Cache-Control" };
    private static final String ALLOWED_ORIGIN = Dotenv.load().get("CORS_ORIGIN");

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(ALLOWED_ORIGIN) // Allow frontend origin
            .allowedMethods(ALLOWED_METHODS)
            .allowedHeaders(ALLOWED_HEADERS)
            .allowCredentials(true); // Allow cookies
    }
}
