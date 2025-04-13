package com.api.business;

import com.api.model.entity.Client;
import com.api.utils.BigDecimalUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceUpdater {

    // atualiza os saldos das contas envolvidas na transacao
    public void updateBalances(Client origin, Client destination, BigDecimal amount) {
        origin.setBalance(BigDecimalUtils.subtract(origin.getBalance(), amount));
        destination.setBalance(BigDecimalUtils.add(destination.getBalance(), amount));
    }
}