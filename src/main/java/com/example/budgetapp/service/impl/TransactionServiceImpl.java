package com.example.budgetapp.service.impl;

import com.example.budgetapp.dto.TransactionDto;
import com.example.budgetapp.entity.Account;
import com.example.budgetapp.entity.Category;
import com.example.budgetapp.entity.Transaction;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.exception.APIException;
import com.example.budgetapp.exception.ResourceNotFoundException;
import com.example.budgetapp.mapper.TransactionMapper;
import com.example.budgetapp.repository.AccountRepository;
import com.example.budgetapp.repository.CategoryRepository;
import com.example.budgetapp.repository.TransactionRepository;
import com.example.budgetapp.repository.UserRepository;
import com.example.budgetapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionDto.Response createTransaction(TransactionDto.Request transactionDto) {
        User user = getCurrentUser();

        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", transactionDto.getAccountId()));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Account does not belong to user");
        }

        Category category = categoryRepository.findById(transactionDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", transactionDto.getCategoryId()));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Category does not belong to user");
        }

        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transaction.setAccount(account);
        transaction.setCategory(category);

        // Update Account Balance
        if ("INCOME".equalsIgnoreCase(category.getType())) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        }
        accountRepository.save(account);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    public TransactionDto.Response getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));

        if (!transaction.getAccount().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Transaction", "id", id);
        }

        return transactionMapper.toResponse(transaction);
    }

    @Override
    public List<TransactionDto.Response> getAllTransactions() {
        User user = getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByAccountUserId(user.getId());
        return transactions.stream().map(transactionMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionDto.Response updateTransaction(Long id, TransactionDto.Request transactionDto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));

        if (!transaction.getAccount().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Transaction", "id", id);
        }

        // Revert old balance
        Account oldAccount = transaction.getAccount();
        if ("INCOME".equalsIgnoreCase(transaction.getCategory().getType())) {
            oldAccount.setBalance(oldAccount.getBalance().subtract(transaction.getAmount()));
        } else {
            oldAccount.setBalance(oldAccount.getBalance().add(transaction.getAmount()));
        }
        accountRepository.save(oldAccount);

        // Update fields
        Account newAccount = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", transactionDto.getAccountId()));
        Category newCategory = categoryRepository.findById(transactionDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", transactionDto.getCategoryId()));

        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(transactionDto.getDate());
        transaction.setNote(transactionDto.getNote());
        transaction.setAccount(newAccount);
        transaction.setCategory(newCategory);

        // Apply new balance
        if ("INCOME".equalsIgnoreCase(newCategory.getType())) {
            newAccount.setBalance(newAccount.getBalance().add(transaction.getAmount()));
        } else {
            newAccount.setBalance(newAccount.getBalance().subtract(transaction.getAmount()));
        }
        accountRepository.save(newAccount);

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));

        if (!transaction.getAccount().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Transaction", "id", id);
        }

        // Revert balance
        Account account = transaction.getAccount();
        if ("INCOME".equalsIgnoreCase(transaction.getCategory().getType())) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }
        accountRepository.save(account);

        transactionRepository.delete(transaction);
    }

    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
