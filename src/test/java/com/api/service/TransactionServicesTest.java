package com.api.service;

import com.api.model.dto.TransactionDTO;
import com.api.model.entity.Client;
import com.api.model.entity.Transaction;
import com.api.model.repository.ClientRepository;
import com.api.model.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServicesTest {

    @Autowired
    private TransactionServices transactionServices;

    @Autowired
    private ClientRepository clientRepository;

    // Limpa a tabela antes de cada teste
    @BeforeEach
    void setup() {
        clientRepository.deleteAll();
    }

    // Teste de transação com sucesso
    @Test
    void testProcessTransaction_success() {
        Client origin = new Client();
        origin.setName("Caio");
        origin.setAccountNumber("123456");
        origin.setBalance(new BigDecimal("1000.00"));

        Client destination = new Client();
        destination.setName("Ana");
        destination.setAccountNumber("223456");
        destination.setBalance(new BigDecimal("200.00"));

        clientRepository.save(origin);
        clientRepository.save(destination);

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("123456");
        dto.setAccountDestination("223456");
        dto.setAmount(100.0);

        Transaction result = transactionServices.processTransaction(dto);

        assertEquals("SUCCESS", result.getTransactionStatus());
        assertEquals(new BigDecimal("900.00"), clientRepository.findByAccountNumber("123456").get().getBalance());
        assertEquals(new BigDecimal("300.00"), clientRepository.findByAccountNumber("223456").get().getBalance());
    }

    // Teste de transação para uma conta que não existe
    @Test
    void testProcessTransaction_accountDoesNotExist() {
        Client origin = new Client();
        origin.setName("Caio");
        origin.setAccountNumber("123456");
        origin.setBalance(new BigDecimal("100.00"));

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("123456");
        dto.setAccountDestination("999999"); // não existe
        dto.setAmount(100.0);

        Transaction result = transactionServices.processTransaction(dto);

        assertEquals("FAILED", result.getTransactionStatus());
    }

    // Teste de transação com saldo insuficiente
    @Test
    void testProcessTransaction_insufficientBalance() {
        Client origin = new Client();
        origin.setName("Caio");
        origin.setAccountNumber("123456");
        origin.setBalance(new BigDecimal("50.00"));

        Client destination = new Client();
        destination.setName("Ana");
        destination.setAccountNumber("223456");
        destination.setBalance(new BigDecimal("50.00"));

        clientRepository.save(origin);
        clientRepository.save(destination);

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("123456");
        dto.setAccountDestination("223456");
        dto.setAmount(100.0); // maior do que o saldo do cliente origem

        Transaction result = transactionServices.processTransaction(dto);

        assertEquals("FAILED", result.getTransactionStatus());
    }

    // Teste de transação com valor de transação fora da regra de negócio
    @Test
    void testProcessTransaction_amountBiggerThan100() {
        Client origin = new Client();
        origin.setName("Caio");
        origin.setAccountNumber("123456");
        origin.setBalance(new BigDecimal("1000.00"));

        Client destination = new Client();
        destination.setName("Ana");
        destination.setAccountNumber("223456");
        destination.setBalance(new BigDecimal("200.00"));

        clientRepository.save(origin);
        clientRepository.save(destination);

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("123456");
        dto.setAccountDestination("223456");
        dto.setAmount(200.00);

        Transaction result = transactionServices.processTransaction(dto);

        assertEquals("FAILED", result.getTransactionStatus());
    }
}