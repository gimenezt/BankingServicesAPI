package com.api.business;

import org.springframework.stereotype.Component;

@Component
public class TransactionAmountValidator {

    // verifica se o valor da transacao esta dentro das regras de negocio
    public boolean isValid(double amount) {
        return amount > 0 && amount <= 100;
    }
}