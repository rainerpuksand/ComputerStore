package com.example.computerstore.repository;

import com.example.computerstore.entity.CartItem;
import com.example.computerstore.entity.Computer;
import com.example.computerstore.service.ConnectionService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.function.Consumer;

public class CartRepository {
    private final EntityManager em;

    public CartRepository() {
        this.em = ConnectionService.getSessionFactory().createEntityManager();
    }


    public CartItem findOne(long id) {
        return em.find(CartItem.class, id);
    }

    public void save(CartItem cartItem) {
        persist(cartItem, em::persist);
    }

    public List<Computer> findItemsByUserId(long id) {
        String hqlQuery = "select c from Computer c " +
                "join CartItem ci on ci.itemId = c.id " +
                "join User u on u.id = ci.userId " +
                "where u.id = ?1";
        TypedQuery<Computer> query = em.createQuery(hqlQuery, Computer.class);
        query.setParameter(1, id);

        return query.getResultList();
    }

    public void clearCart(long id){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Query query = em.createQuery("delete CartItem c where c.userId = :id").
                setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
    }

    public CartItem update(CartItem cartItem) {
        return persist(cartItem, em::merge);
    }

    public void delete(CartItem cartItem) {
        persist(cartItem, em::remove);
    }

    public void decrementCount(long itemId, long userId) {
        String hqlQuery = "from CartItem c where c.itemId = ?1 AND c.userId = ?2";
        TypedQuery<CartItem> query = em.createQuery(hqlQuery, CartItem.class);
        query.setParameter(1, itemId);
        query.setParameter(2, userId);
        List<CartItem> items = query.getResultList();
        if (!items.isEmpty()) {
            this.delete(items.get(0));
        }
    }

    // Functional Interfaces: Predicate, Consumer, Function, Comparator
    private CartItem persist(CartItem cartItem, Consumer<CartItem> consumer) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            consumer.accept(cartItem);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
        return null;
    }
}
