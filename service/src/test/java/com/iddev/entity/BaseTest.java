package com.iddev.entity;

import com.iddev.repository.CarRepository;
import com.iddev.repository.ClientRepository;
import com.iddev.repository.OrderRepository;
import com.iddev.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;

public abstract class BaseTest {

    private static SessionFactory sessionFactory;
    protected static EntityManager entityManager;
    protected CarRepository carRepository = new CarRepository(entityManager);
    protected ClientRepository clientRepository = new ClientRepository(entityManager);
    protected OrderRepository orderRepository = new OrderRepository(entityManager);

    @BeforeAll
    static void setUp() {
        sessionFactory = HibernateUtil.buildSessionFactory();
        entityManager = (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
    }

    @AfterAll
    static void shutDown() {
        sessionFactory.close();
    }

    @BeforeEach
    void openTransaction() {
//        entityManager  = sessionFactory.getCurrentSession();
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void rollBack() {
        entityManager.getTransaction().rollback();
        entityManager.close();
    }
}
