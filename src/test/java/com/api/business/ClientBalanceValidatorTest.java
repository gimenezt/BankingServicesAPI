package com.api.business;

import com.api.model.dto.ClientDTO;
import com.api.model.entity.Client;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;


public class ClientBalanceValidatorTest {
    @Test
    public void testBalanceValidator() {
        ClientBalanceValidator validator = new ClientBalanceValidator();

        ClientBuilder clientBuilder = new ClientBuilder();

        ClientDTO originDTO = new ClientDTO();
        originDTO.setName("Lilian");
        originDTO.setAccountNumber("111");
        originDTO.setBalance(new BigDecimal("100"));
        Client origin = clientBuilder.build(originDTO);

        // Teste para transacionar um valor menor que o saldo
        assertTrue(validator.hasSufficientBalance(origin, new BigDecimal("70.00")));

        // Teste para transacionar um valor igual ao saldo
        assertTrue(validator.hasSufficientBalance(origin, new BigDecimal("100.00")));

        // Teste para transacionar um valor maior que o saldo
        assertFalse(validator.hasSufficientBalance(origin, new BigDecimal("120.00")));
    }
}