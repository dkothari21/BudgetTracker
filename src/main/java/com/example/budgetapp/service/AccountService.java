package com.example.budgetapp.service;

import com.example.budgetapp.dto.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto.Response createAccount(AccountDto.Request accountDto);

    AccountDto.Response getAccountById(Long id);

    List<AccountDto.Response> getAllAccounts();

    AccountDto.Response updateAccount(Long id, AccountDto.Request accountDto);

    void deleteAccount(Long id);
}
