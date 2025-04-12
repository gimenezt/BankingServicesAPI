package com.api.business;

import com.api.model.entity.Client;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceUpdaterTest {
    @Test
    public void testUpdaterBalance() {
        BalanceUpdater balanceUpdater = new BalanceUpdater();

        Client origin = new Client();
        origin.setBalance(new BigDecimal("100"));

        Client destination = new Client();
        destination.setBalance(new BigDecimal("100"));

        BigDecimal transferAmount = new BigDecimal("30");

        balanceUpdater.updateBalances(origin, destination, transferAmount);

        BigDecimal expectedOriginBalance = new BigDecimal("70");
        BigDecimal expectedDestinationBalance = new BigDecimal("130");

        assertEquals(expectedOriginBalance, origin.getBalance());
        assertEquals(expectedDestinationBalance, destination.getBalance());
    }
}
