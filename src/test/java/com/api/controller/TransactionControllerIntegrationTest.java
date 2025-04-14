package com.api.controller;

import com.api.business.ClientBuilder;
import com.api.business.TransactionBuilder;
import com.api.model.dto.TransactionDTO;
import com.api.model.repository.ClientRepository;
import com.api.model.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientBuilder clientBuilder;

    @Autowired
    private TransactionBuilder transactionBuilder;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        clientRepository.deleteAll();
    }

    private void verifyClientBalance(String conta, BigDecimal saldoEsperado) throws Exception {
        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.accountNumber == '" + conta + "')].balance").value(saldoEsperado.doubleValue()));
    }

    @Test
    void createTransaction_success() throws Exception {
        clientBuilder.build("Lilian", "12345", BigDecimal.valueOf(1000));
        clientBuilder.build("Lucas", "54321", BigDecimal.valueOf(500));

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("12345");
        dto.setAccountDestination("54321");
        dto.setAmount(100.00);

        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountOrigin").value("12345"))
                .andExpect(jsonPath("$.accountDestination").value("54321"))
                .andExpect(jsonPath("$.amount").value(100));

        verifyClientBalance("12345", BigDecimal.valueOf(900));
        verifyClientBalance("54321", BigDecimal.valueOf(600));
    }

    @Test
    void getTransactionList_success() throws Exception {
        clientBuilder.build("Lilian", "12345", BigDecimal.valueOf(1000));
        clientBuilder.build("Lucas", "54321", BigDecimal.valueOf(500));

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("12345");
        dto.setAccountDestination("54321");
        dto.setAmount(100.00);
        transactionRepository.save(transactionBuilder.build(dto));

        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].accountOrigin").value("12345"));

        verifyClientBalance("12345", BigDecimal.valueOf(1000));
    }

    @Test
    void getTransactionList_failed() throws Exception {
        clientBuilder.build("Lilian", "12345", BigDecimal.valueOf(1000));
        clientBuilder.build("Lucas", "54321", BigDecimal.valueOf(500));

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("12345");
        dto.setAccountDestination("54321");
        dto.setAmount(150.00);
        transactionRepository.save(transactionBuilder.build(dto));

        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].accountOrigin").value("12345"));

        verifyClientBalance("12345", BigDecimal.valueOf(1000));
        verifyClientBalance("54321", BigDecimal.valueOf(500));
    }

    @Test
    void getTransactionsByAccountOrigin_success() throws Exception {
        clientBuilder.build("Lilian", "12345", BigDecimal.valueOf(1000));
        clientBuilder.build("Lucas", "54321", BigDecimal.valueOf(500));

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("12345");
        dto.setAccountDestination("54321");
        dto.setAmount(50.00);
        transactionRepository.save(transactionBuilder.build(dto));

        mockMvc.perform(get("/transaction/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].accountDestination").value("54321"));

        verifyClientBalance("12345", BigDecimal.valueOf(1000));
    }

    @Test
    void getTransactionsByAccountOrigin_failed() throws Exception {
        clientBuilder.build("Lilian", "12345", BigDecimal.valueOf(1000));
        clientBuilder.build("Lucas", "54321", BigDecimal.valueOf(500));

        TransactionDTO dto = new TransactionDTO();
        dto.setAccountOrigin("12345");
        dto.setAccountDestination("54321");
        dto.setAmount(200.00);
        transactionRepository.save(transactionBuilder.build(dto));

        mockMvc.perform(get("/transaction/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].accountDestination").value("54321"));

        verifyClientBalance("12345", BigDecimal.valueOf(1000));
        verifyClientBalance("54321", BigDecimal.valueOf(500));
    }

    @Test
    void getTransactionsByAccountOrigin_notFound() throws Exception {
        mockMvc.perform(get("/transaction/00000"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhuma transação encontrada para esta conta."));
    }
}