package com.api.business;

import com.api.model.dto.ClientDTO;
import com.api.model.entity.Client;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceUpdaterTest {

    @Test
    public void testUpdaterBalance() {
        BalanceUpdater balanceUpdater = new BalanceUpdater();
        ClientBuilder clientBuilder = new ClientBuilder();

        // Cliente origem
        ClientDTO originDTO = new ClientDTO();
        originDTO.setName("Lilian");
        originDTO.setAccountNumber("111");
        originDTO.setBalance(new BigDecimal("100"));
        Client origin = clientBuilder.build(originDTO);

        // Cliente destino
        ClientDTO destinationDTO = new ClientDTO();
        destinationDTO.setName("Lucas");
        destinationDTO.setAccountNumber("222");
        destinationDTO.setBalance(new BigDecimal("100"));
        Client destination = clientBuilder.build(destinationDTO);

        BigDecimal transferAmount = new BigDecimal("30");

        balanceUpdater.updateBalances(origin, destination, transferAmount);

        BigDecimal expectedOriginBalance = new BigDecimal("70");
        BigDecimal expectedDestinationBalance = new BigDecimal("130");

        assertEquals(expectedOriginBalance, origin.getBalance());
        assertEquals(expectedDestinationBalance, destination.getBalance());
    }
}
