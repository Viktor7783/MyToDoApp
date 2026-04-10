package com.korotkov.todoapp.dao;

import com.korotkov.todoapp.model.ToDo;
import com.korotkov.todoapp.model.User;

import java.util.List;

public interface ToDoDao {
    void insertToDo(ToDo todo);

    ToDo selectToDo(long todoId);

    ToDo selectToDoByIdAndUserId(long todoId, int userId);

    List<ToDo> selectAllToDosByUser(User user);

    void deleteToDo(long todoId);

    void updateToDo(ToDo todo);
}
