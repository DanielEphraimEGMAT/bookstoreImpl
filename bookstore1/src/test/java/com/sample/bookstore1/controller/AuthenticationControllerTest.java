package com.sample.bookstore1.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.sample.bookstore1.model.User;
import com.sample.bookstore1.repository.UserRepository;
import com.sample.bookstore1.service.JwtService;

@WebMvcTest(UserController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @Test
    public void testSignup() throws Exception {
        User user = new User();
        user.setEmail("daniel@test.com");
        user.setFullName("Daniel Ephraim");
        user.setPassword("testPassword");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fullName\":\"John Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testLogin() throws Exception {
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        Mockito.when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateToken(user)).thenReturn("dummyToken");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john.doe@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("dummyToken"));
    }
}