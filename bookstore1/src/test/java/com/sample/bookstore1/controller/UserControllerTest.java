package com.sample.bookstore1.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sample.bookstore1.model.User;
import com.sample.bookstore1.service.UserService;

public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testAuthenticatedUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");

        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        verify(authentication, times(1)).getPrincipal();
        verify(securityContext, times(1)).getAuthentication();
    }

    @Test
    public void testAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setFullName("John Doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setId(2);
        user2.setFullName("Jane Doe");
        user2.setEmail("jane.doe@example.com");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.allUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[1].name", is("Jane Doe")))
                .andExpect(jsonPath("$[1].email", is("jane.doe@example.com")));

        verify(userService, times(1)).allUsers();
        verifyNoMoreInteractions(userService);
    }
}