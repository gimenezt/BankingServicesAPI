package com.api.business;

import com.api.model.dto.ClientDTO;
import com.api.model.entity.Client;

public class ClientBuilder {

    // Cria a instância de Client
    public Client build(ClientDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setAccountNumber(dto.getAccountNumber());
        client.setBalance(dto.getBalance());
        return client;
    }
}
