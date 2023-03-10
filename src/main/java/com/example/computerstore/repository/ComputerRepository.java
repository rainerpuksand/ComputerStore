package com.example.computerstore.repository;

import com.example.computerstore.entity.Computer;
import com.example.computerstore.service.ConnectionService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Consumer;

public class ComputerRepository {

    private final EntityManager em;

    public ComputerRepository() {
        this.em = ConnectionService.getSessionFactory().createEntityManager();
    }

    public List<Computer> findAll() {
        String hqlQuery = "from Computer c";
        return em.createQuery(hqlQuery, Computer.class).getResultList();
    }

    public List<Computer> findAllInStock(){
        String hqlQuery = "from Computer c where c.inStock = 'y'";
        return em.createQuery(hqlQuery, Computer.class).getResultList();
    }

    public void updateStockStatus(long id, String status){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Query query = em.createQuery("update Computer c set c.inStock = :status where c.id = :id").
                setParameter("status", status).
                setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
    }

    public Computer findOne(long id) {
        return em.find(Computer.class, id);
    }

    public void save(Computer computer) {
        persist(computer, em::persist);
    }

    public Computer update(Computer computer) {
        return persist(computer, em::merge);
    }

    public void delete(Computer computer) {
        persist(computer, em::remove);
    }

    // Functional Interfaces: Predicate, Consumer, Function, Comparator
    private Computer persist(Computer computer, Consumer<Computer> consumer) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            consumer.accept(computer);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
        return null;
    }
}
