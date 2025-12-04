package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.CategoryDto;
import com.example.budgetapp.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T21:35:35-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CategoryDto.Request request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.name( request.getName() );
        category.color( request.getColor() );
        category.type( request.getType() );

        return category.build();
    }

    @Override
    public CategoryDto.Response toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto.Response response = new CategoryDto.Response();

        response.setId( category.getId() );
        response.setName( category.getName() );
        response.setColor( category.getColor() );
        response.setType( category.getType() );

        return response;
    }
}
