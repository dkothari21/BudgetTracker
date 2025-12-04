package com.example.budgetapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class CategoryDto {
    @Data
    public static class Request {
        @NotBlank(message = "Category name is required")
        private String name;

        private String color;

        @NotBlank(message = "Category type is required")
        private String type;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String color;
        private String type;
    }
}
