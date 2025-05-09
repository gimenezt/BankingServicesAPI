package com.api.controller;

import com.api.business.ClientBuilder;
import com.api.model.repository.ClientRepository;
import com.api.model.entity.Client;
import com.api.model.dto.ClientDTO;
import com.api.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientBuilder clientBuilder;


    // Registra novos clientes
    @PostMapping
    public ResponseEntity<Client> register(@RequestBody @Valid ClientDTO clientDTO) {

        // Verifica se a conta já existe
        if (clientRepository.existsByAccountNumber(clientDTO.getAccountNumber())) {
            throw new CustomException("Conta já existe.", 400);
        }

        Client client = clientBuilder.build(clientDTO);
        Client savedClient = clientRepository.save(client); // Salva cliente no banco

        URI location = URI.create("/client/" + savedClient.getAccountNumber());
        return ResponseEntity.created(location).body(savedClient);
    }

    // Lista os clientes registrados
    @GetMapping
    public ResponseEntity getClientList() {
        var clientsList = clientRepository.findAll();
        return ResponseEntity.ok(clientsList);
    }

    // Retorna informações do cliente pelo número da conta informado
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