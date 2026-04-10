package com.korotkov.todoapp.web;

import com.korotkov.todoapp.dao.ToDoDao;
import com.korotkov.todoapp.model.ToDo;
import com.korotkov.todoapp.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ToDoControllerTest {

    private final ToDoController controller = new ToDoController();
    private final ToDoDao toDoDao = mock(ToDoDao.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);

    @BeforeEach
    void setUp() {
        controller.setToDoDao(toDoDao);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void doGetListLoadsTodosAndForwardsToListPage() throws ServletException, IOException {
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        User user = User.builder().id(7).username("user").build();
        List<ToDo> todos = List.of(ToDo.builder().title("Task").description("Desc").targetDate(LocalDate.now()).user(user).build());

        when(request.getServletPath()).thenReturn("/list");
        when(session.getAttribute("user")).thenReturn(user);
        when(session.getAttribute("ERROR")).thenReturn("Flash error");
        when(toDoDao.selectAllToDosByUser(user)).thenReturn(todos);
        when(request.getRequestDispatcher("/todo-list.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);

        verify(request).setAttribute("ERROR", "Flash error");
        verify(session).removeAttribute("ERROR");
        verify(request).setAttribute("listToDo", todos);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostInsertForwardsBackWhenValidationFails() throws ServletException, IOException {
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        User user = User.builder().id(7).username("user").build();

        when(request.getServletPath()).thenReturn("/insert");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("title")).thenReturn("   ");
        when(request.getParameter("description")).thenReturn("Description");
        when(request.getRequestDispatcher("/todo-form.jsp")).thenReturn(dispatcher);

        controller.doPost(request, response);

        verify(request).setAttribute("ERROR", "title не может быть пустым");
        verify(dispatcher).forward(request, response);
        verify(toDoDao, never()).insertToDo(any());
    }

    @Test
    void doPostUpdatePersistsOwnedTodoAndRedirects() throws ServletException, IOException {
        User user = User.builder().id(7).username("user").build();
        ToDo todo = ToDo.builder()
                .id(5L)
                .title("Old")
                .description("Old desc")
                .targetDate(LocalDate.of(2026, 3, 1))
                .user(user)
                .build();

        when(request.getServletPath()).thenReturn("/update");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("id")).thenReturn("5");
        when(toDoDao.selectToDoByIdAndUserId(5L, 7)).thenReturn(todo);
        when(request.getParameter("title")).thenReturn(" New title ");
        when(request.getParameter("description")).thenReturn(" New desc ");
        when(request.getParameter("targetDate")).thenReturn("2026-03-30");
        when(request.getParameter("isDone")).thenReturn("true");
        when(request.getContextPath()).thenReturn("/todo");

        controller.doPost(request, response);

        verify(toDoDao).updateToDo(todo);
        verify(response).sendRedirect("/todo/list");
    }

    @Test
    void doPostDeleteRedirectsWhenTodoDoesNotBelongToUser() throws ServletException, IOException {
        User user = User.builder().id(7).username("user").build();

        when(request.getServletPath()).thenReturn("/delete");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("id")).thenReturn("5");
        when(toDoDao.selectToDoByIdAndUserId(5L, 7)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/todo");

        controller.doPost(request, response);

        verify(response).sendRedirect("/todo/list");
        verify(toDoDao, never()).deleteToDo(anyLong());
    }
}
