package com.api.business;

import com.api.model.entity.Client;
import com.api.utils.BigDecimalUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ClientBalanceValidator {

    // Verifica se o cliente possui saldo suficiente para transação
    public boolean hasSufficientBalance(Client origin, BigDecimal amount) {
        return !BigDecimalUtils.isLessThan(origin.getBalance(), amount);
    }
}