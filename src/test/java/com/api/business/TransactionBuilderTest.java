package com.api.business;

import com.api.model.entity.Transaction;
import com.api.model.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TransactionBuilderTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    // Constrói a instância de Transaction
    public void testBuildTransaction() {
        TransactionBuilder builder = new TransactionBuilder();

        Transaction transaction = builder.build("123456", "654321", 200.00);
        transactionRepository.save(transaction);

        assertEquals("123456", transaction.getAccountOrigin());
        assertEquals("654321", transaction.getAccountDestination());
        assertEquals(200.00, transaction.getAmount());
    }
}
