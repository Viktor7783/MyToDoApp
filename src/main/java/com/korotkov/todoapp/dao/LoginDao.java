package com.korotkov.todoapp.dao;

import com.korotkov.todoapp.model.LoginBean;
import com.korotkov.todoapp.model.User;
import com.korotkov.todoapp.utils.HibernateUtils;
import org.hibernate.Session;

public class LoginDao {
    public User findByUsername(LoginBean loginBean) throws ClassNotFoundException {

        User user;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            user = session.createQuery("from User where username = :username", User.class)
                    .setParameter("username", loginBean.getUsername())
                    .uniqueResultOptional().orElse(null);
        }

        return user;
    }
}

