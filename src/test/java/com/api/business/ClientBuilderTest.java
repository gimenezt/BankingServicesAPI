package com.api.business;

import com.api.model.dto.ClientDTO;
import com.api.model.entity.Client;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ClientBuilderTest {

    @Test
    void testBuildClient() {
        ClientBuilder builder = new ClientBuilder();

        ClientDTO dto = new ClientDTO();
        dto.setName("Ana");
        dto.setBalance(new BigDecimal("100"));
        dto.setAccountNumber("123654");

        Client client = builder.build(dto);
        assertEquals("Ana", client.getName());
        assertEquals(new BigDecimal("100"), client.getBalance());
        assertEquals("123654", client.getAccountNumber());
    }
}