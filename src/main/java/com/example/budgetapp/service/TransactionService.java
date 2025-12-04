package com.example.budgetapp.service;

import com.example.budgetapp.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto.Response createTransaction(TransactionDto.Request transactionDto);

    TransactionDto.Response getTransactionById(Long id);

    List<TransactionDto.Response> getAllTransactions();

    TransactionDto.Response updateTransaction(Long id, TransactionDto.Request transactionDto);

    void deleteTransaction(Long id);
}
