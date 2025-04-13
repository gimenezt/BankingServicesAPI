package com.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransactionDTO {
    @NotBlank(message = "O 'accountOrigin' não pode ser nulo ou vazio")
    private String accountOrigin;

    @NotBlank(message = "O 'accountDestination' não pode ser nulo ou vazio")
    private String accountDestination;

    @NotNull(message = "O 'amount' não pode ser nulo ou vazio")
    private Double amount;

    // getters e setters
    public String getAccountOrigin() {
        return accountOrigin;
    }

    public void setAccountOrigin(String accountOrigin) {
        this.accountOrigin = accountOrigin;
    }

    public String getAccountDestination() {
        return accountDestination;
    }

    public void setAccountDestination(String accountDestination) {
        this.accountDestination = accountDestination;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}