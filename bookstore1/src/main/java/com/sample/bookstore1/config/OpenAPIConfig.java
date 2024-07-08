package com.sample.bookstore1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        // Adding Header info to swagger docs
        Info info = new Info()
                .title("Bookstore API")
                .version("1.0")
                .description("This API exposes endpoints to manage books and reviews.");

        return new OpenAPI().info(info);
    }
}
