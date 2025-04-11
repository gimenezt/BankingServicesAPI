package com.api.business;

import com.api.model.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientExistsValidator {

    @Autowired
    private ClientRepository clientRepository;

    public boolean clientsExist(String originAccount, String destinationAccount) {
        return clientRepository.findByAccountNumber(originAccount).isPresent()
                && clientRepository.findByAccountNumber(destinationAccount).isPresent();
    }
}