package com.api.controllers;

import com.api.domain.transaction.Transaction;
import com.api.domain.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    // Retorna lista de transações por numero de conta origem
    @GetMapping("/{accountOrigin}")
    public ResponseEntity findByAccountOrigin(@PathVariable @Valid String accountOrigin) {
        List<Transaction> transaction = transactionRepository.findByAccountOriginOrderByDatetimeDesc(accountOrigin);

        if (transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
