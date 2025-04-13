package com.api.business;

import org.springframework.stereotype.Component;

@Component
public class AccountEqualValidator {
    // Verifica se conta origem e destino são iguais
    public boolean equal(String originAccountNumber, String destinationAccountNumber) {
        return originAccountNumber.equals(destinationAccountNumber);
    }
}