package com.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String accountOrigin;

    @NotBlank
    private String accountDestination;

    private String transactionStatus;

    @NotNull
    private Double amount;

    @NotNull
    private LocalDateTime datetime = LocalDateTime.now();

    // getters e setters
    public Long getId() { return id; }

    public String getAccountOrigin() { return accountOrigin; }
    public void setAccountOrigin(String accountOrigin) { this.accountOrigin = accountOrigin; }

    public String getTransactionStatus() { return transactionStatus; }
    public void setTransactionStatus(String transactionStatus) { this.transactionStatus = transactionStatus; }

    public String getAccountDestination() { return accountDestination; }
    public void setAccountDestination(String accountDestination) { this.accountDestination = accountDestination; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getDatetime() { return datetime; }
}
