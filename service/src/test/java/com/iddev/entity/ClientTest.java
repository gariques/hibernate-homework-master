package com.iddev.entity;

import com.iddev.enums.Role;
import com.iddev.filters.ClientFilter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientTest extends BaseTest {

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
        assertEquals(client, actualClient.get());
    }

    @Test
    void updateClient() {
        var client = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();
        clientRepository.save(client);
        client.setDriverLicenseId("123");
        entityManager.clear();

        clientRepository.update(client);

        var updatedClient = clientRepository.findById(client.getId());

        assertThat(updatedClient.get()).isEqualTo(client);
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

        var clients = clientRepository.getClientsByFirstAndLastnames(entityManager, filter, clientGraph);
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getLastName()).isEqualTo("Testoff");
    }

}
