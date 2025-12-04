package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.TransactionDto;
import com.example.budgetapp.entity.Account;
import com.example.budgetapp.entity.Category;
import com.example.budgetapp.entity.Transaction;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T21:35:35-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public Transaction toEntity(TransactionDto.Request request) {
        if ( request == null ) {
            return null;
        }

        Transaction.TransactionBuilder transaction = Transaction.builder();

        transaction.amount( request.getAmount() );
        transaction.date( request.getDate() );
        transaction.note( request.getNote() );

        return transaction.build();
    }

    @Override
    public TransactionDto.Response toResponse(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionDto.Response response = new TransactionDto.Response();

        response.setAccountName( transactionAccountName( transaction ) );
        response.setCategoryName( transactionCategoryName( transaction ) );
        response.setCategoryType( transactionCategoryType( transaction ) );
        response.setId( transaction.getId() );
        response.setAmount( transaction.getAmount() );
        response.setDate( transaction.getDate() );
        response.setNote( transaction.getNote() );

        return response;
    }

    private String transactionAccountName(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        Account account = transaction.getAccount();
        if ( account == null ) {
            return null;
        }
        String name = account.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String transactionCategoryName(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        Category category = transaction.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String transactionCategoryType(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        Category category = transaction.getCategory();
        if ( category == null ) {
            return null;
        }
        String type = category.getType();
        if ( type == null ) {
            return null;
        }
        return type;
    }
}
