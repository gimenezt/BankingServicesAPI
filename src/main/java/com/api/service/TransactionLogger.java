package com.api.service;

import com.api.model.entity.Transaction;
import com.api.model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionLogger {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailedTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}