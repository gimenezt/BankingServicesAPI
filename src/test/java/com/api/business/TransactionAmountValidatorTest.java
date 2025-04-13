package com.api.business;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionAmountValidatorTest {
    @Test
    public void validateTransactionAmount() {
        TransactionAmountValidator validator = new TransactionAmountValidator();

        // Regra de negócio: valor de transação > 0 e <= 100

        assertFalse(validator.isValid(-10));    // transacionar valor menor que 0
        assertFalse(validator.isValid(0));      // transacionar valor igual a 0
        assertTrue(validator.isValid(10));      // transacionar valor maior que 0 menor que 100
        assertTrue(validator.isValid(100));     // transacionar valor igual a 100
        assertFalse(validator.isValid(110));;   // transacionar valor maior que 100
    }
}
