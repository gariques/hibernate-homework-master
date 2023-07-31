package com.iddev.config;

import com.iddev.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;

@Configuration
@ComponentScan(basePackages = "com.iddev")
public class ApplicationConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }

    @Bean
    public EntityManager entityManager(SessionFactory sessionFactory) {
        return (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{EntityManager.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
    }

    @PreDestroy
    public void closeResources() {
        sessionFactory().close();
    }
}
