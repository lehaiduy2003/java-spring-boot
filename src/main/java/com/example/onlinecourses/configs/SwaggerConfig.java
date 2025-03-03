package com.example.onlinecourses.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Online Courses API", version = "v1", description = "APIs for Online Courses Application"),
    servers = @Server(url = "http://localhost:8080")
)
public class SwaggerConfig {

}
