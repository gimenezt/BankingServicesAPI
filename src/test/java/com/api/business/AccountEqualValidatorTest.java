package com.api.business;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountEqualValidatorTest {

    private final AccountEqualValidator validator = new AccountEqualValidator();

    // Teste para quando as contas são iguais
    @Test
    void whenAccountsAreEqual_true() {
        String origin = "123456";
        String destination = "123456";

        boolean result = validator.equal(origin, destination);

        assertTrue(result, "Should return true when accounts are equal");
    }

    // Teste para quando as contas são diferentes
    @Test
    void whenAccountsAreDifferent_false() {
        String origin = "123456";
        String destination = "654321";

        boolean result = validator.equal(origin, destination);

        assertFalse(result, "Should return false when accounts are different");
    }
}