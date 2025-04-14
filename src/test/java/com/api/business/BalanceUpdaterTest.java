package com.api.business;

import com.api.model.dto.ClientDTO;
import com.api.model.entity.Client;

import com.api.model.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class BalanceUpdaterTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testUpdaterBalance() {
        BalanceUpdater balanceUpdater = new BalanceUpdater();
        ClientBuilder clientBuilder = new ClientBuilder();

        Client origin = clientBuilder.build("Lilian","111",BigDecimal.valueOf(100));
        clientRepository.save(origin);

        Client destination = clientBuilder.build("Lucas","222",BigDecimal.valueOf(100));
        clientRepository.save(destination);

        BigDecimal transferAmount = BigDecimal.valueOf(30);

        balanceUpdater.updateBalances(origin, destination, transferAmount);

        BigDecimal expectedOriginBalance = BigDecimal.valueOf(70);
        BigDecimal expectedDestinationBalance = BigDecimal.valueOf(130);

        assertEquals(expectedOriginBalance, origin.getBalance());
        assertEquals(expectedDestinationBalance, destination.getBalance());
    }
}
