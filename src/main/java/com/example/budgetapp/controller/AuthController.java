package com.example.budgetapp.controller;

import com.example.budgetapp.dto.UserDto;
import com.example.budgetapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto.AuthResponse> login(@RequestBody UserDto.LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(new UserDto.AuthResponse(token, null, loginRequest.getUsername(), null));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto.RegistrationRequest registrationRequest) {
        String response = authService.register(registrationRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
