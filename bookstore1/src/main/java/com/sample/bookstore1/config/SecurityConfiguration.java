package com.sample.bookstore1.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor to inject the JwtAuthenticationFilter and AuthenticationProvider
    // dependencies
    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Defines the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .cors(cors -> cors.disable()) // Disable CORS
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()) // Require authentication for all other requests
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider) // Use the custom authentication provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Build the security filter chain
    }

    // Defines the CORS configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // Create a new CORS configuration

        configuration.setAllowedOrigins(List.of("http://localhost:8080")); // Allow requests from this origin
        configuration.setAllowedMethods(List.of("GET", "POST")); // Allow these HTTP methods
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Allow these headers

        // Create a URL-based CORSconfiguration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Register the CORS configuration for all paths

        return source; // Return the CORS configuration source
    }
}
