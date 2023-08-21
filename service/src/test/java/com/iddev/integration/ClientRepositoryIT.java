package com.iddev.integration;

import com.iddev.annotation.IT;
import com.iddev.entity.Client;
import com.iddev.enums.Role;
import com.iddev.filters.ClientFilter;
import com.iddev.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class ClientRepositoryIT extends IntegrationTestBase {

    private final EntityManager entityManager;
    private final ClientRepository clientRepository;

    @Test
    void saveClient() {
        var expectedResult = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();

        clientRepository.save(expectedResult);

        assertNotNull(expectedResult.getId());

    }

    @Test
    void readClient() {
        var client = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();

        clientRepository.save(client);
        entityManager.clear();

        var actualClient = clientRepository.findById(client.getId());

        assertNotNull(client.getId());
        assertTrue(actualClient.isPresent());
        assertEquals(client, actualClient.get());
    }

    @Test
    void deleteClient() {
        var client = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();

        clientRepository.save(client);
        clientRepository.delete(client);

        assertThat(clientRepository.findById(client.getId()).isEmpty()).isTrue();
    }

    @Test
    void getClientsWithFilter() {
        var clientGraph = entityManager.createEntityGraph(Client.class);

        var client = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();
        var client2 = Client.builder()
                .firstName("Vasya")
                .lastName("Pupkin")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();

        clientRepository.save(client);
        clientRepository.save(client2);

        ClientFilter filter = ClientFilter.builder()
                .firstName("Test")
                .lastName("Testoff")
                .build();

        var clients = clientRepository.findClientsByFirstAndLastnames(entityManager, filter, clientGraph);
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getLastName()).isEqualTo("Testoff");
    }

}
