package com.api.business;

import org.springframework.stereotype.Component;

@Component
public class AccountEqualValidator {
    // Verifica se conta origem e destino não são igual
    public boolean equal(String originAccountNumber, String destinationAccountNumber) {
        return originAccountNumber == destinationAccountNumber;
    }
}
