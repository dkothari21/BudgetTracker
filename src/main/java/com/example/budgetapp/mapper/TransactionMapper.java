package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.TransactionDto;
import com.example.budgetapp.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    Transaction toEntity(TransactionDto.Request request);

    @Mapping(source = "account.name", target = "accountName")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.type", target = "categoryType")
    TransactionDto.Response toResponse(Transaction transaction);
}
