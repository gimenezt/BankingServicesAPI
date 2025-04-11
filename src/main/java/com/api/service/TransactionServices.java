package com.api.service;

import com.api.business.*;
import com.api.model.dto.TransactionDTO;
import com.api.model.entity.Client;
import com.api.model.entity.Transaction;
import com.api.model.repository.ClientRepository;
import com.api.model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionServices {

    @Autowired
    private ClientExistsValidator clientExistsValidator;

    @Autowired
    private TransactionAmountValidator transactionAmountValidator;

    @Autowired
    private ClientBalanceValidator clientBalanceValidator;

    @Autowired
    private BalanceUpdater balanceUpdater;

    @Autowired
    private TransactionBuilder transactionBuilder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final Object lockObject = new Object();

    @Transactional
    public Transaction processTransaction(TransactionDTO dto) {
        synchronized (lockObject) {     // para tratar concorrencia
            Transaction transaction = transactionBuilder.build(dto);

            // validando existencia das contas
            if (!clientExistsValidator.clientsExist(dto.getAccountOrigin(), dto.getAccountDestination())) {
                transaction.setTransactionStatus("FAILED");
                return transactionRepository.save(transaction);
            }

            // validando de o valor a transacionar eh valido
            if (!transactionAmountValidator.isValid(dto.getAmount())) {
                transaction.setTransactionStatus("FAILED");
                return transactionRepository.save(transaction);
            }

            Client origin = clientRepository.findByAccountNumber(dto.getAccountOrigin()).get();
            Client destination = clientRepository.findByAccountNumber(dto.getAccountDestination()).get();
            BigDecimal amount = BigDecimal.valueOf(dto.getAmount());

            if (!clientBalanceValidator.hasSufficientBalance(origin, amount)) {
                transaction.setTransactionStatus("FAILED");
                return transactionRepository.save(transaction);
            }

            balanceUpdater.updateBalances(origin, destination, amount);
            clientRepository.save(origin);
            clientRepository.save(destination);

            transaction.setTransactionStatus("SUCCESS");
            return transactionRepository.save(transaction);
        }
    }
}