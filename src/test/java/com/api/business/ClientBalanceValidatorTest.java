package com.api.business;

import com.api.model.dto.ClientDTO;
import com.api.model.entity.Client;

import com.api.model.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ClientBalanceValidatorTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testBalanceValidator() {
        ClientBalanceValidator validator = new ClientBalanceValidator();
        ClientBuilder clientBuilder = new ClientBuilder();

        Client origin = clientBuilder.build("Lilian","111",BigDecimal.valueOf(100));
        clientRepository.save(origin);

        // Teste para transacionar um valor menor que o saldo
        assertTrue(validator.hasSufficientBalance(origin, BigDecimal.valueOf(70)));

        // Teste para transacionar um valor igual ao saldo
        assertTrue(validator.hasSufficientBalance(origin, BigDecimal.valueOf(100)));

        // Teste para transacionar um valor maior que o saldo
        assertFalse(validator.hasSufficientBalance(origin, BigDecimal.valueOf(120)));
    }
}