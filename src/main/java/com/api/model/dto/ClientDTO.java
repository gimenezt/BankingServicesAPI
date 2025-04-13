package com.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ClientDTO {
    @NotBlank(message = "O 'name' não pode ser nulo ou vazio")
    private String name;

    @NotBlank(message = "O 'accountNumber' não pode ser nulo ou vazio")
    private String accountNumber;

    @NotNull(message = "O 'balance' não pode ser nulo ou vazio")
    private BigDecimal balance;

    // getters e setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}