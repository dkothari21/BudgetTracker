package com.example.budgetapp.service.impl;

import com.example.budgetapp.dto.BudgetDto;
import com.example.budgetapp.entity.Budget;
import com.example.budgetapp.entity.Category;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.exception.APIException;
import com.example.budgetapp.exception.ResourceNotFoundException;
import com.example.budgetapp.mapper.BudgetMapper;
import com.example.budgetapp.repository.BudgetRepository;
import com.example.budgetapp.repository.CategoryRepository;
import com.example.budgetapp.repository.UserRepository;
import com.example.budgetapp.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BudgetMapper budgetMapper;

    @Override
    public BudgetDto.Response createBudget(BudgetDto.Request budgetDto) {
        User user = getCurrentUser();

        Category category = categoryRepository.findById(budgetDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", budgetDto.getCategoryId()));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Category does not belong to user");
        }

        if (budgetRepository.findByCategoryIdAndMonth(category.getId(), budgetDto.getMonth()).isPresent()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Budget already exists for this category and month");
        }

        Budget budget = budgetMapper.toEntity(budgetDto);
        budget.setCategory(category);

        Budget savedBudget = budgetRepository.save(budget);
        return budgetMapper.toResponse(savedBudget);
    }

    @Override
    public BudgetDto.Response getBudgetById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "id", id));

        if (!budget.getCategory().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Budget", "id", id);
        }

        return budgetMapper.toResponse(budget);
    }

    @Override
    public List<BudgetDto.Response> getAllBudgets() {
        User user = getCurrentUser();
        List<Budget> budgets = budgetRepository.findByCategoryUserId(user.getId());
        return budgets.stream().map(budgetMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public BudgetDto.Response updateBudget(Long id, BudgetDto.Request budgetDto) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "id", id));

        if (!budget.getCategory().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Budget", "id", id);
        }

        budget.setAmount(budgetDto.getAmount());
        // Typically month and category shouldn't change for an existing budget ID, but
        // if needed logic can be added here.

        Budget updatedBudget = budgetRepository.save(budget);
        return budgetMapper.toResponse(updatedBudget);
    }

    @Override
    public void deleteBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "id", id));

        if (!budget.getCategory().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Budget", "id", id);
        }

        budgetRepository.delete(budget);
    }

    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
