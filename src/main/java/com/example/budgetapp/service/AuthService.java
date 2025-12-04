package com.example.budgetapp.service;

import com.example.budgetapp.dto.UserDto;

public interface AuthService {
    String login(UserDto.LoginRequest loginRequest);

    String register(UserDto.RegistrationRequest registrationRequest);
}
