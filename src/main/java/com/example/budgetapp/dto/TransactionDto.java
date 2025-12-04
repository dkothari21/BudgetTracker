package com.example.budgetapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {
    @Data
    public static class Request {
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotNull(message = "Date is required")
        private LocalDate date;

        private String note;

        @NotNull(message = "Account ID is required")
        private Long accountId;

        @NotNull(message = "Category ID is required")
        private Long categoryId;
    }

    @Data
    public static class Response {
        private Long id;
        private BigDecimal amount;
        private LocalDate date;
        private String note;
        private String accountName;
        private String categoryName;
        private String categoryType;
    }
}
