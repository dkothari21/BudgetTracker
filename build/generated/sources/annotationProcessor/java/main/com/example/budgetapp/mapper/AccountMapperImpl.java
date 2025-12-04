package com.example.budgetapp.mapper;

import com.example.budgetapp.dto.AccountDto;
import com.example.budgetapp.entity.Account;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T21:35:35-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toEntity(AccountDto.Request request) {
        if ( request == null ) {
            return null;
        }

        Account.AccountBuilder account = Account.builder();

        account.name( request.getName() );
        account.type( request.getType() );
        account.balance( request.getBalance() );

        return account.build();
    }

    @Override
    public AccountDto.Response toResponse(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDto.Response response = new AccountDto.Response();

        response.setId( account.getId() );
        response.setName( account.getName() );
        response.setType( account.getType() );
        response.setBalance( account.getBalance() );

        return response;
    }
}
