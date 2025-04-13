package com.api.controller;

import com.api.model.entity.Transaction;
import com.api.model.repository.TransactionRepository;
import com.api.model.dto.TransactionDTO;
import com.api.service.TransactionServices;
import com.api.exception.CustomException;
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
    public ResponseEntity<?> findByAccountOrigin(@PathVariable @Valid String accountOrigin) {
        List<Transaction> transactions = transactionRepository.findByAccountOriginOrderByDatetimeDesc(accountOrigin);

        if (transactions.isEmpty()) {
            throw new CustomException("Nenhuma transação encontrada para esta conta.", 404);
        }

        return ResponseEntity.ok(transactions);
    }

    // Criar nova transação
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        Transaction transaction = transactionServices.processTransaction(transactionDTO);
        return ResponseEntity.ok(transaction);
    }

    // Lista todas as transações
    @GetMapping
    public ResponseEntity<?> getTransactionList() {
        var transactionList = transactionRepository.findAll();
        return ResponseEntity.ok(transactionList);
    }
}