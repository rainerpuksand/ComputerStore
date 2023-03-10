package com.example.computerstore.service;

import com.example.computerstore.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public final class ConnectionService {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Computer.class);
            configuration.addAnnotatedClass(CartItem.class);
            configuration.addAnnotatedClass(Bill.class);
            configuration.addAnnotatedClass(BillItem.class);

            sessionFactory = configuration.buildSessionFactory(
                    new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build()
            );
        }
        return sessionFactory;
    }
}
