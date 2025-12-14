package com.example.budgetapp.service.impl;

import com.example.budgetapp.dto.UserDto;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.exception.APIException;
import com.example.budgetapp.mapper.UserMapper;
import com.example.budgetapp.repository.UserRepository;
import com.example.budgetapp.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserDto.LoginRequest loginRequest;
    private UserDto.RegistrationRequest registrationRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        loginRequest = new UserDto.LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        registrationRequest = new UserDto.RegistrationRequest();
        registrationRequest.setUsername("newuser");
        registrationRequest.setEmail("newuser@example.com");
        registrationRequest.setPassword("password123");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("newuser");
        testUser.setEmail("newuser@example.com");
        testUser.setPassword("encodedPassword");
    }

    @Test
    @DisplayName("Should login successfully and return JWT token")
    void login_Success() {
        // Arrange
        String expectedToken = "jwt.token.here";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(expectedToken);

        // Act
        String token = authService.login(loginRequest);

        // Assert
        assertThat(token).isEqualTo(expectedToken);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(authentication);
    }

    @Test
    @DisplayName("Should register user successfully")
    void register_Success() {
        // Arrange
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(userMapper.toEntity(registrationRequest)).thenReturn(testUser);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        String result = authService.register(registrationRequest);

        // Assert
        assertThat(result).contains("User registered successfully");
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void register_UsernameExists() {
        // Arrange
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(APIException.class)
                .hasMessageContaining("Username is already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void register_EmailExists() {
        // Arrange
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(APIException.class)
                .hasMessageContaining("Email is already exists");

        verify(userRepository, never()).save(any(User.class));
    }
}
