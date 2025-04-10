package com.api.controllers;

import com.api.domain.transaction.Transaction;
import com.api.domain.transaction.TransactionRepository;
import com.api.dto.TransactionDTO;
import com.api.services.TransactionServices;
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

    @Autowired
    private TransactionServices transactionServices;

    // Retorna lista de transações por numero de conta origem
    @GetMapping("/{accountOrigin}")
    public ResponseEntity findByAccountOrigin(@PathVariable @Valid String accountOrigin) {
        List<Transaction> transaction = transactionRepository.findByAccountOriginOrderByDatetimeDesc(accountOrigin);

        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transaction);
        }
    }
}
