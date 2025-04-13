package com.api.service;

import com.api.business.*;
import com.api.exception.CustomException;
import com.api.model.dto.TransactionDTO;
import com.api.model.entity.Client;
import com.api.model.entity.Transaction;
import com.api.model.repository.ClientRepository;
import com.api.model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.api.service.TransactionLogger;

import java.math.BigDecimal;

@Service
public class TransactionServices {

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

    @Autowired
    private AccountEqualValidator accountEqualValidator;

    @Autowired
    private TransactionLogger transactionLogger;

    private final Object lockObject = new Object();

    @Transactional
    public Transaction processTransaction(TransactionDTO dto) {
        synchronized (lockObject) {     // para tratar concorrência
            Transaction transaction = transactionBuilder.build(dto);

            // Valida se conta origem existe
            if (!clientRepository.existsByAccountNumber(dto.getAccountOrigin())) {
                transaction.setTransactionStatus("FAILED");
                transactionLogger.logFailedTransaction(transaction);
                throw new CustomException("Conta de origem não encontrada.", 404);
            }

            // Valida se conta destino existe
            if (!clientRepository.existsByAccountNumber(dto.getAccountDestination())) {
                transaction.setTransactionStatus("FAILED");
                transactionLogger.logFailedTransaction(transaction);
                throw new CustomException("Conta de destino não encontrada.", 404);
            }

            // Valida se contas são iguais
            if (accountEqualValidator.equal(dto.getAccountOrigin(), dto.getAccountDestination())){
                transaction.setTransactionStatus("FAILED");
                transactionLogger.logFailedTransaction(transaction);
                throw new CustomException("As contas origem e destino não podem ser iguais", 404);
            }

            // Valida valor da transação
            if (!transactionAmountValidator.isValid(dto.getAmount())) {
                transaction.setTransactionStatus("FAILED");
                transactionLogger.logFailedTransaction(transaction);
                throw new CustomException("Valor da transação inválido.", 400);
            }

            // Busca clientes origem e destino e define valor da transação
            Client origin = clientRepository.findByAccountNumber(dto.getAccountOrigin()).get();
            Client destination = clientRepository.findByAccountNumber(dto.getAccountDestination()).get();
            BigDecimal amount = BigDecimal.valueOf(dto.getAmount());

            // Valida saldo
            if (!clientBalanceValidator.hasSufficientBalance(origin, amount)) {
                transaction.setTransactionStatus("FAILED");
                transactionLogger.logFailedTransaction(transaction);
                throw new CustomException("Saldo insuficiente para realizar a transação.", 400);
            }

            // Realiza atualização de saldos
            balanceUpdater.updateBalances(origin, destination, amount);
            clientRepository.save(origin);
            clientRepository.save(destination);

            transaction.setTransactionStatus("SUCCESS");
            return transactionRepository.save(transaction);
        }
    }
}