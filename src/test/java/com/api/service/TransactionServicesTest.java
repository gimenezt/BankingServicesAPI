package com.api.service;

import com.api.business.ClientBuilder;
import com.api.exception.CustomException;
import com.api.model.dto.TransactionDTO;
import com.api.model.entity.Client;
import com.api.model.entity.Transaction;
import com.api.model.repository.ClientRepository;
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

    @Autowired
    private ClientBuilder clientBuilder;

    // Limpa a tabela antes de cada teste
    @BeforeEach
    void setup() {
        clientRepository.deleteAll();
    }

    // Teste de transação com sucesso
    @Test
    void testProcessTransaction_success() {
        Client origin = clientBuilder.build("Lilian", "123456", BigDecimal.valueOf(1000));
        Client destination = clientBuilder.build("Lucas", "223456", BigDecimal.valueOf(200));

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
        Client origin = clientBuilder.build("Caio", "123456", BigDecimal.valueOf(100));
        clientRepository.save(origin);

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("123456");
        dto.setAccountDestination("999999"); // conta inexistente
        dto.setAmount(100.0);

        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionServices.processTransaction(dto);
        });

        assertEquals("Conta de destino não encontrada.", exception.getMessage());
    }

    // Teste de transação com saldo insuficiente
    @Test
    void testProcessTransaction_insufficientBalance() {
        Client origin = clientBuilder.build("Caio", "123456", BigDecimal.valueOf(50));
        Client destination = clientBuilder.build("Ana", "223456", BigDecimal.valueOf(50));

        clientRepository.save(origin);
        clientRepository.save(destination);

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("123456");
        dto.setAccountDestination("223456");
        dto.setAmount(100.0); // maior do que o saldo do cliente origem

        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionServices.processTransaction(dto);
        });

        assertEquals("Saldo insuficiente para realizar a transação.", exception.getMessage());
    }

    // Teste de transação com valor de transação fora da regra de negócio
    @Test
    void testProcessTransaction_amountBiggerThan100() {
        Client origin = clientBuilder.build("Caio", "123456", BigDecimal.valueOf(1000));
        Client destination = clientBuilder.build("Ana", "223456", BigDecimal.valueOf(200));

        clientRepository.save(origin);
        clientRepository.save(destination);

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("123456");
        dto.setAccountDestination("223456");
        dto.setAmount(200.00);

        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionServices.processTransaction(dto);
        });

        assertEquals("Valor da transação inválido.", exception.getMessage());
    }
}