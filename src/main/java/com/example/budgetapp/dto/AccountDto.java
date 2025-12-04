package com.example.budgetapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

public class AccountDto {
    @Data
    public static class Request {
        @NotBlank(message = "Account name is required")
        private String name;

        @NotBlank(message = "Account type is required")
        private String type;

        @NotNull(message = "Balance is required")
        private BigDecimal balance;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String type;
        private BigDecimal balance;
    }
}
