package com.api.services;

import com.api.domain.client.Client;
import com.api.domain.client.ClientRepository;
import com.api.domain.transaction.Transaction;
import com.api.domain.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionServices {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction processTransaction(String accountOrigin, String accountDestination, Double amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountOrigin(accountOrigin);
        transaction.setAccountDestination(accountDestination);
        transaction.setAmount(amount);

        Optional<Client> originClient = clientRepository.findByAccountNumber(accountOrigin);
        Optional<Client> destinationClient = clientRepository.findByAccountNumber(accountDestination);

        // se a conta origem ou conta destino for vazia, salva como transacao falha
        if (originClient.isEmpty() || destinationClient.isEmpty()) {
            transaction.setTransactionStatus("FAILED");
            return transactionRepository.save(transaction);
        }

        // se a transacao for acima de 100 reais, salva como transacao falha
        if (amount > 100) {
            transaction.setTransactionStatus("FAILED");
            return transactionRepository.save(transaction);
        }

        // se o saldo da conta origem for menor do que a transacao, salva como transacao falha
        if (originClient.get().getBalance() < amount) {
            transaction.setTransactionStatus("FAILED");
            return transactionRepository.save(transaction);
        }

        Client origin = originClient.orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));
        Client destination = destinationClient.orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

        origin.setBalance(origin.getBalance() - amount);
        destination.setBalance(destination.getBalance() + amount);

        clientRepository.save(origin);
        clientRepository.save(destination);

        transaction.setTransactionStatus("SUCCESS");
        transactionRepository.save(transaction);

        return transaction;
    }
}