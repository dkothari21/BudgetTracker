package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.BudgetDto;
import com.example.budgetapp.entity.Budget;
import com.example.budgetapp.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T21:35:34-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class BudgetMapperImpl implements BudgetMapper {

    @Override
    public Budget toEntity(BudgetDto.Request request) {
        if ( request == null ) {
            return null;
        }

        Budget.BudgetBuilder budget = Budget.builder();

        budget.amount( request.getAmount() );
        budget.month( request.getMonth() );

        return budget.build();
    }

    @Override
    public BudgetDto.Response toResponse(Budget budget) {
        if ( budget == null ) {
            return null;
        }

        BudgetDto.Response response = new BudgetDto.Response();

        response.setCategoryName( budgetCategoryName( budget ) );
        response.setId( budget.getId() );
        response.setAmount( budget.getAmount() );
        response.setMonth( budget.getMonth() );

        return response;
    }

    private String budgetCategoryName(Budget budget) {
        if ( budget == null ) {
            return null;
        }
        Category category = budget.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
