package com.api.model.repository;

import com.api.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByAccountNumber(String accountNumber);
    Optional<Client> findByAccountNumber(String accountNumber);
}