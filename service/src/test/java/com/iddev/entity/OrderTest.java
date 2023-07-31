package com.iddev.entity;

import com.iddev.enums.CarStatus;
import com.iddev.enums.Role;
import com.iddev.filters.ClientFilter;
import org.junit.jupiter.api.Test;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderTest extends BaseTest {

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
        assertEquals(order, actualResult.get());
    }

    @Test
    void updateOrder() {
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

        order.setFinishDate(Instant.now().plusSeconds(172800).truncatedTo(ChronoUnit.HOURS));
        entityManager.clear();
        orderRepository.update(order);

        var updatedOrder = orderRepository.findById(order.getId());

        assertThat(updatedOrder.get()).isEqualTo(order);
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
        List<Order> orderList = orderRepository.getOrdersByFirstAndLastnames(entityManager, filter, orderGraph);

        assertThat(orderList).hasSize(2);
        assertThat(orderList.get(0).getClient().getLastName()).isEqualTo("Testoff");
        assertThat(orderList.get(1).getClient().getLastName()).isEqualTo("Testoff");
    }
}