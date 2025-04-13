package com.api.business;

import org.springframework.stereotype.Component;

@Component
public class TransactionAmountValidator {

    // Verifica se o valor da transação está dentro das regras de negócio
    public boolean isValid(double amount) {
        return amount > 0 && amount <= 100;
    }
}