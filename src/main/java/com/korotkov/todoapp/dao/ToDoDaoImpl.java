package com.korotkov.todoapp.dao;

import com.korotkov.todoapp.model.ToDo;
import com.korotkov.todoapp.model.User;
import com.korotkov.todoapp.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ToDoDaoImpl implements ToDoDao {

    @Override
    public void insertToDo(ToDo todo) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(todo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка создания ToDo", e);
        }
    }

    @Override
    public ToDo selectToDo(long todoId) {

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.find(ToDo.class, todoId);
        }
    }

    @Override
    public ToDo selectToDoByIdAndUserId(long todoId, int userId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from ToDo td where td.id=:todoId and td.user.id=:userId", ToDo.class).setParameter("todoId", todoId).setParameter("userId", userId).uniqueResult();
        }
    }

    @Override
    public List<ToDo> selectAllToDosByUser(User user) {

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from ToDo td where td.user.id=:userId", ToDo.class).setParameter("userId", user.getId()).getResultList();
        }
    }

    @Override
    public void deleteToDo(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("delete from ToDo where id=:id").setParameter("id", id).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка удаления ToDo", e);
        }
    }

    @Override
    public void updateToDo(ToDo toDo) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(toDo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Ошибка обновления ToDo", e);
        }
    }
}