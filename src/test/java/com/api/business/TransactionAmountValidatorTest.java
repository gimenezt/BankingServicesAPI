package com.api.business;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionAmountValidatorTest {
    @Test
    public void validateTransactionAmount() {
        TransactionAmountValidator validator = new TransactionAmountValidator();

        assertFalse(validator.isValid(-10));
        assertFalse(validator.isValid(0));
        assertTrue(validator.isValid(10));
        assertTrue(validator.isValid(100));
        assertFalse(validator.isValid(110));;

    }
}
