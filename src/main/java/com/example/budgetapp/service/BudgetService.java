package com.example.budgetapp.service;

import com.example.budgetapp.dto.BudgetDto;

import java.util.List;

public interface BudgetService {
    BudgetDto.Response createBudget(BudgetDto.Request budgetDto);

    BudgetDto.Response getBudgetById(Long id);

    List<BudgetDto.Response> getAllBudgets();

    BudgetDto.Response updateBudget(Long id, BudgetDto.Request budgetDto);

    void deleteBudget(Long id);
}
