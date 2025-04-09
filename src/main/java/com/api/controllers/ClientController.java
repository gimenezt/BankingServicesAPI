package com.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.domain.client.ClientRepository;
import com.api.domain.client.Client;
import jakarta.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    // Lista de clientes
    @GetMapping
    public ResponseEntity getClientList() {
        var clientsList = clientRepository.findAll();
        return ResponseEntity.ok(clientsList);
    }

    // Cadastrar cliente
    @PostMapping
    public ResponseEntity register(@RequestBody @Valid Client client) {
        if (clientRepository.existsByAccountNumber(client.getAccountNumber())) {
            return ResponseEntity.badRequest().body(".");
        }

        Client saveObj = clientRepository.save(client);
        return ResponseEntity.ok(saveObj);
    }

    // Retorna cliente pelo numero da conta
    @GetMapping("/{accountNumber}")
    public ResponseEntity findByAccountNumber(@PathVariable @Valid String accountNumber) {
        Optional<Client> client = clientRepository.findByAccountNumber(accountNumber);

        if (client.isPresent()) {
            return ResponseEntity.ok(client.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
