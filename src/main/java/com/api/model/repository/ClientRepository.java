package com.api.model.repository;

import com.api.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // boolean: verifica se o cliente existe pelo numero da conta
    boolean existsByAccountNumber(String accountNumber);

    // Client: traz informações do cliente pelo numero da conta
    Optional<Client> findByAccountNumber(String accountNumber);
}