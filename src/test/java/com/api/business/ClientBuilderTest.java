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
class ClientBuilderTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testBuildClient() {
        ClientBuilder builder = new ClientBuilder();

        Client client = builder.build("Ana","123654",BigDecimal.valueOf(100));
        clientRepository.save(client);

        assertEquals("Ana", client.getName());
        assertEquals(new BigDecimal("100"), client.getBalance());
        assertEquals("123654", client.getAccountNumber());
    }
}