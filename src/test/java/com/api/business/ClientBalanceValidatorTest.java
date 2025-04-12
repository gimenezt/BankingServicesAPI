package com.api.business;

import com.api.model.entity.Client;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;


public class ClientBalanceValidatorTest {
    @Test
    public void tesBalanceValidator() {
        ClientBalanceValidator validator = new ClientBalanceValidator();

        Client origin = new Client();
        origin.setBalance(new BigDecimal("100.00"));

        assertTrue(validator.hasSufficientBalance(origin, new BigDecimal("70.00")));
        assertTrue(validator.hasSufficientBalance(origin, new BigDecimal("100.00")));
        assertFalse(validator.hasSufficientBalance(origin, new BigDecimal("120.00")));
    }
}