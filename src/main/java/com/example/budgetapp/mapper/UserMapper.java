package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.UserDto;
import com.example.budgetapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "categories", ignore = true)
    User toEntity(UserDto.RegistrationRequest request);

    UserDto.Response toResponse(User user);
}
