package com.example.budgetapp.service.impl;

import com.example.budgetapp.dto.CategoryDto;
import com.example.budgetapp.entity.Category;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.exception.ResourceNotFoundException;
import com.example.budgetapp.mapper.CategoryMapper;
import com.example.budgetapp.repository.CategoryRepository;
import com.example.budgetapp.repository.UserRepository;
import com.example.budgetapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto.Response createCategory(CategoryDto.Request categoryDto) {
        User user = getCurrentUser();
        Category category = categoryMapper.toEntity(categoryDto);
        category.setUser(user);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryDto.Response getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        if (!category.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryDto.Response> getAllCategories() {
        User user = getCurrentUser();
        List<Category> categories = categoryRepository.findByUserId(user.getId());
        return categories.stream().map(categoryMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public CategoryDto.Response updateCategory(Long id, CategoryDto.Request categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (!category.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Category", "id", id);
        }

        category.setName(categoryDto.getName());
        category.setColor(categoryDto.getColor());
        category.setType(categoryDto.getType());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (!category.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Category", "id", id);
        }

        categoryRepository.delete(category);
    }

    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
