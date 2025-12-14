package com.example.budgetapp.controller;

import com.example.budgetapp.dto.UserDto;
import com.example.budgetapp.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AuthController Integration Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private UserDto.LoginRequest loginRequest;
    private UserDto.RegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new UserDto.LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        registrationRequest = new UserDto.RegistrationRequest();
        registrationRequest.setUsername("newuser");
        registrationRequest.setEmail("newuser@example.com");
        registrationRequest.setPassword("password123");
    }

    @Test
    @DisplayName("Should login successfully")
    void login_Success() throws Exception {
        // Arrange
        String token = "jwt.token.here";
        when(authService.login(any(UserDto.LoginRequest.class))).thenReturn(token);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("Should register successfully")
    void register_Success() throws Exception {
        // Arrange
        String message = "User registered successfully!.";
        when(authService.register(any(UserDto.RegistrationRequest.class))).thenReturn(message);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(message));
    }

    @Test
    @DisplayName("Should return 400 when login with invalid data")
    void login_InvalidData() throws Exception {
        // Arrange
        UserDto.LoginRequest invalidRequest = new UserDto.LoginRequest();
        // Missing username and password

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when registering with invalid email")
    void register_InvalidEmail() throws Exception {
        // Arrange
        registrationRequest.setEmail("invalid-email");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());
    }
}
