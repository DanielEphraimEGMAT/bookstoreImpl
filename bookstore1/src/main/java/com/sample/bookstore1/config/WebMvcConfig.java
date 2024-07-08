package com.sample.bookstore1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitingInterceptor rateLimitingInterceptor;

    // Adds the rate limiting interceptor to the registry of interceptors
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor); // Register the rate limiting interceptor
    }

}