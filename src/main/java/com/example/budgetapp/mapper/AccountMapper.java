package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.AccountDto;
import com.example.budgetapp.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Account toEntity(AccountDto.Request request);

    AccountDto.Response toResponse(Account account);
}
