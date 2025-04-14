package com.api.business;

import com.api.model.dto.TransactionDTO;
import com.api.model.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionBuilder {

    // Cria a instância de transaction
    public Transaction build(TransactionDTO dto) {
        return build(dto.getAccountOrigin(), dto.getAccountDestination(), dto.getAmount());
    }

    public Transaction build(String accountOrigin, String accountDestination, Double amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountOrigin(accountOrigin);
        transaction.setAccountDestination(accountDestination);
        transaction.setAmount(amount);
        return transaction;
    }
}