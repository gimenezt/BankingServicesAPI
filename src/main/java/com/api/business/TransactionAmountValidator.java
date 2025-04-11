package com.api.business;

import org.springframework.stereotype.Component;

@Component
public class TransactionAmountValidator {

    public boolean isValid(double amount) {
        return amount > 0 && amount <= 100;
    }
}