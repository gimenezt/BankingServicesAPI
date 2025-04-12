package com.api.business;

import com.api.model.dto.TransactionDTO;
import com.api.model.entity.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionBuilderTest {

    @Test
    public void testBuildTransaction() {
        TransactionBuilder builder = new TransactionBuilder();

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("123456");
        dto.setAccountDestination("654321");
        dto.setAmount(200.00);

        Transaction transaction = builder.build(dto);

        assertEquals("123456", transaction.getAccountOrigin());
        assertEquals("654321", transaction.getAccountDestination());
        assertEquals(200.00, transaction.getAmount());
    }
}
