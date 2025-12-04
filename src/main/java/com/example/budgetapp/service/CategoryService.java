package com.example.budgetapp.service;

import com.example.budgetapp.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto.Response createCategory(CategoryDto.Request categoryDto);

    CategoryDto.Response getCategoryById(Long id);

    List<CategoryDto.Response> getAllCategories();

    CategoryDto.Response updateCategory(Long id, CategoryDto.Request categoryDto);

    void deleteCategory(Long id);
}
