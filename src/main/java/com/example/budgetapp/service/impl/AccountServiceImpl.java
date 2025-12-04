package com.example.budgetapp.service.impl;

import com.example.budgetapp.dto.AccountDto;
import com.example.budgetapp.entity.Account;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.exception.ResourceNotFoundException;
import com.example.budgetapp.mapper.AccountMapper;
import com.example.budgetapp.repository.AccountRepository;
import com.example.budgetapp.repository.UserRepository;
import com.example.budgetapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountDto.Response createAccount(AccountDto.Request accountDto) {
        User user = getCurrentUser();
        Account account = accountMapper.toEntity(accountDto);
        account.setUser(user);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    @Override
    public AccountDto.Response getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
        // Ensure account belongs to current user
        if (!account.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Account", "id", id);
        }
        return accountMapper.toResponse(account);
    }

    @Override
    public List<AccountDto.Response> getAllAccounts() {
        User user = getCurrentUser();
        List<Account> accounts = accountRepository.findByUserId(user.getId());
        return accounts.stream().map(accountMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public AccountDto.Response updateAccount(Long id, AccountDto.Request accountDto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));

        if (!account.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Account", "id", id);
        }

        account.setName(accountDto.getName());
        account.setType(accountDto.getType());
        account.setBalance(accountDto.getBalance());

        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toResponse(updatedAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));

        if (!account.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Account", "id", id);
        }

        accountRepository.delete(account);
    }

    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
