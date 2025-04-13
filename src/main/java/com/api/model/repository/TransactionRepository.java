package com.api.model.repository;

import com.api.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Lista: traz todas as transações de uma conta em ordem decrescente
    List<Transaction> findByAccountOriginOrderByDatetimeDesc(String accountOrigin);
}
