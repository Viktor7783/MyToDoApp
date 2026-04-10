package com.korotkov.todoapp.dao;

import com.korotkov.todoapp.model.User;
import com.korotkov.todoapp.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;


public class UserDao {

    public void registerUser(User user) {
        //Возьми обычный пароль, добавь соль, захешируй его 2^12 раз и верни безопасный хеш для хранения в базе.
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));

        user.setPassword(hashedPassword);

        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка регистрации User", e);
        }
    }

    public User getUserByName(String username) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from User where username=:username", User.class).setParameter("username", username).uniqueResult();
        }
    }
}