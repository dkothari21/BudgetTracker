package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.CategoryDto;
import com.example.budgetapp.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "budget", ignore = true)
    Category toEntity(CategoryDto.Request request);

    CategoryDto.Response toResponse(Category category);
}
