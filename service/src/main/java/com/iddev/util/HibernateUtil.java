package com.iddev.util;

import com.iddev.entity.Car;
import com.iddev.entity.Client;
import com.iddev.entity.Order;
import com.iddev.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        var configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(User.class);
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
