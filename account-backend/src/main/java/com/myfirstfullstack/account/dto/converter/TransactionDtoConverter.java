package com.myfirstfullstack.account.dto.converter;

import com.myfirstfullstack.account.dto.TransactionDto;
import com.myfirstfullstack.account.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionDtoConverter {

    public TransactionDto convert(Transaction from) {
        return new TransactionDto(from.getId(),
                from.getTransactionType(),
                from.getAmount(),
                from.getTransactionDate());
    }
}