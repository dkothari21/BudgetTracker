package com.example.budgetapp.repository;

import com.example.budgetapp.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByCategoryUserId(Long userId);

    Optional<Budget> findByCategoryIdAndMonth(Long categoryId, YearMonth month);
}
