package com.iddev.integration;

import com.iddev.annotation.IT;
import com.iddev.entity.Car;
import com.iddev.entity.Client;
import com.iddev.entity.Order;
import com.iddev.enums.CarStatus;
import com.iddev.enums.Role;
import com.iddev.filters.ClientFilter;
import com.iddev.repository.CarRepository;
import com.iddev.repository.ClientRepository;
import com.iddev.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;


import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class OrderRepositoryIT extends IntegrationTestBase {

    private final EntityManager entityManager;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;

    @Test
    void saveOrder() {
        var client = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();

        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();

        var order = Order.builder()
                .startDate(Instant.now())
                .finishDate(Instant.now().plusSeconds(86400))
                .build();
        order.setClient(client);
        order.setCar(car);

        clientRepository.save(client);
        carRepository.save(car);
        orderRepository.save(order);

        assertNotNull(order.getId());
    }

    @Test
    void readOrder() {
        var client = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();

        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();

        var order = Order.builder()
                .startDate(Instant.now().truncatedTo(ChronoUnit.HOURS))
                .finishDate(Instant.now().plusSeconds(86400).truncatedTo(ChronoUnit.HOURS))
                .build();
        order.setClient(client);
        order.setCar(car);

        clientRepository.save(client);
        carRepository.save(car);
        orderRepository.save(order);
        entityManager.clear();

        var actualResult = orderRepository.findById(order.getId());

        assertNotNull(order.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(order, actualResult.get());
    }

    @Test
    void deleteOrder() {
        var client = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();

        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();

        var order = Order.builder()
                .startDate(Instant.now().truncatedTo(ChronoUnit.HOURS))
                .finishDate(Instant.now().plusSeconds(86400).truncatedTo(ChronoUnit.HOURS))
                .build();
        order.setCar(car);
        order.setClient(client);

        clientRepository.save(client);
        carRepository.save(car);
        orderRepository.save(order);

        orderRepository.delete(order);

        assertThat(orderRepository.findById(order.getId()).isEmpty()).isTrue();
    }

    @Test
    void getOrdersByFirstAndLastnames() {
        var orderGraph = entityManager.createEntityGraph(Order.class);

        var client1 = Client.builder()
                .firstName("Test")
                .lastName("Testoff")
                .login("test@gmail.com")
                .driverLicenseId("123")
                .password("111")
                .role(Role.CLIENT)
                .build();
        var client2 = Client.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .login("test2@gmail.com")
                .driverLicenseId("1234")
                .password("1113")
                .role(Role.CLIENT)
                .build();

        var car1 = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var car2 = Car.builder()
                .model("Toyota Camry 70")
                .colour("black")
                .price(3500)
                .status(CarStatus.AVAILABLE)
                .build();

        var order1 = Order.builder()
                .startDate(Instant.now().truncatedTo(ChronoUnit.HOURS))
                .finishDate(Instant.now().plusSeconds(86400).truncatedTo(ChronoUnit.HOURS))
                .build();
        var order2 = Order.builder()
                .startDate(Instant.now().truncatedTo(ChronoUnit.HOURS))
                .finishDate(Instant.now().plusSeconds(86400).truncatedTo(ChronoUnit.HOURS))
                .build();
        var order3 = Order.builder()
                .startDate(Instant.now().truncatedTo(ChronoUnit.HOURS))
                .finishDate(Instant.now().plusSeconds(86400).truncatedTo(ChronoUnit.HOURS))
                .build();
        order1.setCar(car1);
        order1.setClient(client1);
        order2.setCar(car2);
        order2.setClient(client1);
        order3.setCar(car2);
        order3.setClient(client2);

        clientRepository.save(client1);
        clientRepository.save(client2);
        carRepository.save(car1);
        carRepository.save(car2);
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        ClientFilter filter = ClientFilter.builder()
                .firstName("Test")
                .lastName("Testoff")
                .build();
        List<Order> orderList = orderRepository.findOrdersByFirstAndLastnames(entityManager, filter, orderGraph);

        assertThat(orderList).hasSize(2);
        assertThat(orderList.get(0).getClient().getLastName()).isEqualTo("Testoff");
        assertThat(orderList.get(1).getClient().getLastName()).isEqualTo("Testoff");
    }
}