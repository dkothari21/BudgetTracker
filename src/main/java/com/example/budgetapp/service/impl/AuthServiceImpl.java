package com.example.budgetapp.service.impl;

import com.example.budgetapp.dto.UserDto;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.exception.APIException;
import com.example.budgetapp.mapper.UserMapper;
import com.example.budgetapp.repository.UserRepository;
import com.example.budgetapp.security.JwtTokenProvider;
import com.example.budgetapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    public String login(UserDto.LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(UserDto.RegistrationRequest registrationRequest) {
        // Check for username exists
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // Check for email exists
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = userMapper.toEntity(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        userRepository.save(user);

        return "User registered successfully!.";
    }
}
