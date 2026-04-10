package com.korotkov.todoapp.web;

import com.korotkov.todoapp.dao.UserDao;
import com.korotkov.todoapp.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final UserController controller = new UserController();
    private final UserDao userDao = mock(UserDao.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

    UserControllerTest() {
        controller.setUserDao(userDao);
    }

    @Test
    void doGetForwardsToRegisterPage() throws ServletException, IOException {
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostForwardsBackWhenValidationFails() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("Ivan");
        when(request.getParameter("lastName")).thenReturn("Petrov");
        when(request.getParameter("username")).thenReturn("taken_user");
        when(request.getParameter("password")).thenReturn("Passw0rd!");
        when(userDao.getUserByName("taken_user")).thenReturn(User.builder().id(1).username("taken_user").build());
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        controller.doPost(request, response);

        verify(request).setAttribute("ERROR", "Пользователь уже существует");
        verify(request).setAttribute("username", "taken_user");
        verify(dispatcher).forward(request, response);
        verify(userDao, never()).registerUser(any());
    }

    @Test
    void doPostRegistersUserAndRedirectsWhenDataIsValid() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("firstName")).thenReturn(" Ivan ");
        when(request.getParameter("lastName")).thenReturn(" Petrov ");
        when(request.getParameter("username")).thenReturn(" new_user ");
        when(request.getParameter("password")).thenReturn(" Passw0rd! ");
        when(userDao.getUserByName("new_user")).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/todo");

        controller.doPost(request, response);

        verify(userDao).registerUser(any(User.class));
        verify(session).setAttribute(eq("user"), any(User.class));
        verify(response).sendRedirect("/todo/list");
    }

    @Test
    void doPostForwardsWithGenericErrorWhenDaoThrows() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("Ivan");
        when(request.getParameter("lastName")).thenReturn("Petrov");
        when(request.getParameter("username")).thenReturn("new_user");
        when(request.getParameter("password")).thenReturn("Passw0rd!");
        when(userDao.getUserByName("new_user")).thenReturn(null);
        doThrow(new RuntimeException("db error")).when(userDao).registerUser(any(User.class));
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        controller.doPost(request, response);

        verify(request).setAttribute("ERROR", "Ошибка при регистрации");
        verify(dispatcher).forward(request, response);
    }
}
