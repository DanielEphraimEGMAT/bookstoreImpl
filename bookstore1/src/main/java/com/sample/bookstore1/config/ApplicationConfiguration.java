package com.sample.bookstore1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sample.bookstore1.repository.UserRepository;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    // Constructor injection for UserRepository
    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Bean definition for UserDetailsService
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Bean definition for BCryptPasswordEncoder
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean definition for AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean definition for AuthenticationProvider
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService()); // Sets the UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Sets the PasswordEncoder

        return authProvider;
    }
}