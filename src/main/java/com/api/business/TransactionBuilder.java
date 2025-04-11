package com.api.business;

import com.api.model.dto.TransactionDTO;
import com.api.model.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionBuilder {

    public Transaction build(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAccountOrigin(dto.getAccountOrigin());
        transaction.setAccountDestination(dto.getAccountDestination());
        transaction.setAmount(dto.getAmount());
        return transaction;
    }
}