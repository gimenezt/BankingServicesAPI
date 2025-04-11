package com.api.controller;

import com.api.model.entity.Transaction;
import com.api.model.repository.TransactionRepository;
import com.api.model.dto.TransactionDTO;
import com.api.service.TransactionServices;
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


    @PostMapping
    public ResponseEntity createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        Transaction transaction = transactionServices.processTransaction(transactionDTO);
        return ResponseEntity.ok(transaction);
    }


    // Lista de transacoes
    @GetMapping
    public ResponseEntity getTransactionList() {
        var transactionList = transactionRepository.findAll();
        return ResponseEntity.ok(transactionList);
    }
}
