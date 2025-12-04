package com.example.budgetapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

public class BudgetDto {
    @Data
    public static class Request {
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotNull(message = "Month is required")
        private YearMonth month;

        @NotNull(message = "Category ID is required")
        private Long categoryId;
    }

    @Data
    public static class Response {
        private Long id;
        private BigDecimal amount;
        private YearMonth month;
        private String categoryName;
    }
}
