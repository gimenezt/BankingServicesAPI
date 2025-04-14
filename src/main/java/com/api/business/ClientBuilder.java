package com.api.business;

import com.api.model.dto.ClientDTO;
import com.api.model.entity.Client;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ClientBuilder {

    public Client build(ClientDTO dto) {
        return build(dto.getName(), dto.getAccountNumber(), dto.getBalance());
    }

    public Client build(String name, String accountNumber, BigDecimal balance) {
        Client client = new Client();
        client.setName(name);
        client.setAccountNumber(accountNumber);
        client.setBalance(balance);
        return client;
    }
}