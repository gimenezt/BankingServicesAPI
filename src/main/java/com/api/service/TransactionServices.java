package com.api.service;

import com.api.business.BalanceUpdater;
import com.api.business.ClientBalanceValidator;
import com.api.business.TransactionAmountValidator;
import com.api.business.TransactionBuilder;
import com.api.exception.CustomException;
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

            // Valida se contas existem
            if (!clientRepository.existsByAccountNumber(dto.getAccountOrigin())) {
                transaction.setTransactionStatus("FAILED");
                transactionRepository.save(transaction);
                throw new CustomException("Conta de origem não encontrada.", 404);
            }

            if (!clientRepository.existsByAccountNumber(dto.getAccountDestination())) {
                transaction.setTransactionStatus("FAILED");
                transactionRepository.save(transaction);
                throw new CustomException("Conta de destino não encontrada.", 404);
            }

            // Valida valor da transação
            if (!transactionAmountValidator.isValid(dto.getAmount())) {
                transaction.setTransactionStatus("FAILED");
                transactionRepository.save(transaction);
                throw new CustomException("Valor da transação inválido.", 400);
            }

            // Busca clientes
            Client origin = clientRepository.findByAccountNumber(dto.getAccountOrigin()).get();
            Client destination = clientRepository.findByAccountNumber(dto.getAccountDestination()).get();

            BigDecimal amount = BigDecimal.valueOf(dto.getAmount());

            // Valida saldo
            if (!clientBalanceValidator.hasSufficientBalance(origin, amount)) {
                transaction.setTransactionStatus("FAILED");
                transactionRepository.save(transaction);
                throw new CustomException("Saldo insuficiente para realizar a transação.", 400);
            }

            balanceUpdater.updateBalances(origin, destination, amount);

            clientRepository.save(origin);
            clientRepository.save(destination);

            transaction.setTransactionStatus("SUCCESS");
            return transactionRepository.save(transaction);
        }
    }
}