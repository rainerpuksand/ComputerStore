package com.example.computerstore.repository;

import com.example.computerstore.entity.User;
import com.example.computerstore.service.ConnectionService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.function.Consumer;

public class UserRepository {

    private final EntityManager em;

    public UserRepository() {
        this.em = ConnectionService.getSessionFactory().createEntityManager();
    }

    public List<User> findAll() {
        String hqlQuery = "from User u";
        return em.createQuery(hqlQuery, User.class).getResultList();
    }

    public boolean emailExists(String email) {
        String hqlQuery = "from User u where u.emailAddress like ?1";
        TypedQuery<User> query = em.createQuery(hqlQuery, User.class);
        query.setParameter(1, email);
        try {
            query.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User loginUser(String email, String password) {
        String hqlQuery = "from User u where u.emailAddress like ?1 AND u.password like ?2";
        TypedQuery<User> query = em.createQuery(hqlQuery, User.class);
        query.setParameter(1, email);
        query.setParameter(2, password);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void updateUserPassword(long id, String newPassword) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Query query = em.createQuery("update User u set u.password = :password where u.id = :id").
                setParameter("password", newPassword).
                setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
    }

    public User findOne(long id) {
        return em.find(User.class, id);
    }

    public User save(User user) {
        return persist(user, em::persist);
    }

    public User update(User user) {
        return persist(user, em::merge);
    }

    public void delete(User user) {
        persist(user, em::remove);
    }

    // Functional Interfaces: Predicate, Consumer, Function, Comparator
    private User persist(User user, Consumer<User> consumer) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            consumer.accept(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
        return null;
    }
}
