package com.api.service;

import com.api.model.entity.Client;
import com.api.model.repository.ClientRepository;
import com.api.model.entity.Transaction;
import com.api.model.repository.TransactionRepository;
import com.api.model.dto.TransactionDTO;
import com.api.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionServices {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final Object lockObject = new Object();

    @Transactional
    public Transaction processTransaction(TransactionDTO dto) {
        synchronized (lockObject) {
            Transaction transaction = new Transaction();
            transaction.setAccountOrigin(dto.getAccountOrigin());
            transaction.setAccountDestination(dto.getAccountDestination());
            transaction.setAmount(dto.getAmount());

            Optional<Client> originClient = clientRepository.findByAccountNumber(dto.getAccountOrigin());
            Optional<Client> destinationClient = clientRepository.findByAccountNumber(dto.getAccountDestination());

            if (originClient.isEmpty() || destinationClient.isEmpty()) {
                transaction.setTransactionStatus("FAILED");
                return transactionRepository.save(transaction);
            }

            if (dto.getAmount() > 100 || dto.getAmount() <= 0) {
                transaction.setTransactionStatus("FAILED");
                return transactionRepository.save(transaction);
            }

            Client origin = originClient.get();
            Client destination = destinationClient.get();

            BigDecimal amountAsBigDecimal = BigDecimal.valueOf(dto.getAmount());

            if (BigDecimalUtils.isLessThan(origin.getBalance(), amountAsBigDecimal)) {
                transaction.setTransactionStatus("FAILED");
                return transactionRepository.save(transaction);
            }

            origin.setBalance(BigDecimalUtils.subtract(origin.getBalance(), amountAsBigDecimal));
            destination.setBalance(BigDecimalUtils.add(destination.getBalance(), amountAsBigDecimal));

            clientRepository.save(origin);
            clientRepository.save(destination);

            transaction.setTransactionStatus("SUCCESS");
            return transactionRepository.save(transaction);
        }
    }
}