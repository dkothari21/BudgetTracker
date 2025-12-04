package com.example.budgetapp.controller;

import com.example.budgetapp.dto.BudgetDto;
import com.example.budgetapp.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetDto.Response> createBudget(
            @Valid @RequestBody BudgetDto.Request budgetDto) {
        return new ResponseEntity<>(budgetService.createBudget(budgetDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetDto.Response> getBudgetById(
            @PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @GetMapping
    public ResponseEntity<List<BudgetDto.Response>> getAllBudgets() {
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDto.Response> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetDto.Request budgetDto) {
        return ResponseEntity.ok(budgetService.updateBudget(id, budgetDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBudget(
            @PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.ok("Budget deleted successfully!");
    }
}
