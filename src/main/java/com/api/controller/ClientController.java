package com.api.controller;

import com.api.model.repository.ClientRepository;
import com.api.model.entity.Client;
import com.api.model.dto.ClientDTO;
import com.api.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    // Cadastrar cliente
    @PostMapping
    public ResponseEntity register(@RequestBody @Valid ClientDTO clientDTO) {
        if (clientRepository.existsByAccountNumber(clientDTO.getAccountNumber())) {
            throw new CustomException("Conta já existe.", 400);
        }

        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setAccountNumber(clientDTO.getAccountNumber());
        client.setBalance(clientDTO.getBalance());

        // Salva o cliente no banco
        Client savedClient = clientRepository.save(client);
        return ResponseEntity.ok(savedClient);
    }

    // Lista de clientes
    @GetMapping
    public ResponseEntity getClientList() {
        var clientsList = clientRepository.findAll();
        return ResponseEntity.ok(clientsList);
    }

    // Retorna cliente pelo numero da conta
    @GetMapping("/{accountNumber}")
    public ResponseEntity findByAccountNumber(@PathVariable @Valid String accountNumber) {
        Optional<Client> client = clientRepository.findByAccountNumber(accountNumber);

        if (client.isPresent()) {
            return ResponseEntity.ok(client.get());
        } else {
            throw new CustomException("Cliente não encontrado.", 404);
        }
    }
}