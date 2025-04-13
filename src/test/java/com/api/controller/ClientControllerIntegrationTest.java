package com.api.controller;

import com.api.model.dto.ClientDTO;
import com.api.model.entity.Client;
import com.api.model.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll(); // limpa o banco H2 antes de cada teste
    }

    @Test

    // Registro com sucesso de cliente
    void registerClient_success() throws Exception {
        ClientDTO client = new ClientDTO();
        client.setName("Lilian");
        client.setAccountNumber("12345");
        client.setBalance(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lilian"))
                .andExpect(jsonPath("$.accountNumber").value("12345"))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    // Teste para registro de contas duplicadas
    void registerClient_duplicateAccountNumber() throws Exception {
        Client existingClient = new Client();
        existingClient.setName("Ana");
        existingClient.setAccountNumber("99999");
        existingClient.setBalance(BigDecimal.valueOf(2000));
        clientRepository.save(existingClient);

        ClientDTO duplicateClient = new ClientDTO();
        duplicateClient.setName("João");
        duplicateClient.setAccountNumber("99999"); // mesmo número
        duplicateClient.setBalance(BigDecimal.valueOf(500));

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateClient)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Conta já existe."));
    }

    @Test
    // Teste para verificar retorno de lista de clientes
    void clientList() throws Exception {
        Client client1 = new Client();
        client1.setName("Carlos");
        client1.setAccountNumber("77777");
        client1.setBalance(BigDecimal.valueOf(1500));

        Client client2 = new Client();
        client2.setName("Mariana");
        client2.setAccountNumber("88888");
        client2.setBalance(BigDecimal.valueOf(2500));

        Client client3 = new Client();
        client3.setName("João");
        client3.setAccountNumber("99999");
        client3.setBalance(BigDecimal.valueOf(3500));

        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);

        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].accountNumber").value("77777"))
                .andExpect(jsonPath("$[1].accountNumber").value("88888"))
                .andExpect(jsonPath("$[2].accountNumber").value("99999"));
    }


    @Test
    // Teste para trazer informações do cliente que existe pelo número de conta
    void findClient_success() throws Exception {
        Client client = new Client();
        client.setName("Julia");
        client.setAccountNumber("88888");
        client.setBalance(BigDecimal.valueOf(3000));
        clientRepository.save(client);

        mockMvc.perform(get("/client/88888"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Julia"));
    }

    @Test
    // Teste para trazer informações do cliente que não existe pelo número de conta
    void findClient_clientDoesNotExists() throws Exception {
        mockMvc.perform(get("/client/00000"))
                .andExpect(status().isNotFound());
    }
}