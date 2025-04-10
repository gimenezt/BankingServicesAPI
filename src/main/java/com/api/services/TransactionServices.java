package com.api.services;

import com.api.domain.client.Client;
import com.api.domain.client.ClientRepository;
import com.api.domain.transaction.Transaction;
import com.api.domain.transaction.TransactionRepository;
import com.api.dto.TransactionDTO;
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

    @Transactional
    public Transaction processTransaction(TransactionDTO dto) {
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
