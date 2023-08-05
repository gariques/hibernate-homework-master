package com.iddev.entity;

import com.iddev.config.ApplicationConfiguration;
import com.iddev.repository.CarRepository;
import com.iddev.repository.ClientRepository;
import com.iddev.repository.OrderRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;

public abstract class BaseTest {

    protected static AnnotationConfigApplicationContext context;

    protected static EntityManager entityManager;

    protected CarRepository carRepository = context.getBean(CarRepository.class);
    protected ClientRepository clientRepository = context.getBean(ClientRepository.class);
    protected OrderRepository orderRepository = context.getBean(OrderRepository.class);

    @BeforeAll
    static void setUp() {
        context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        entityManager = context.getBean(EntityManager.class);
    }

    @AfterAll
    static void shutDown() {
        context.close();
    }

    @BeforeEach
    void openTransaction() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void rollBack() {
        entityManager.getTransaction().rollback();
        entityManager.close();
    }
}
