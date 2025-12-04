package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.BudgetDto;
import com.example.budgetapp.entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Budget toEntity(BudgetDto.Request request);

    @Mapping(source = "category.name", target = "categoryName")
    BudgetDto.Response toResponse(Budget budget);
}
