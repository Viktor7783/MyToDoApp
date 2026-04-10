package com.korotkov.todoapp.web;

import com.korotkov.todoapp.dao.LoginDao;
import com.korotkov.todoapp.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private final LoginController controller = new LoginController();
    private final LoginDao loginDao = mock(LoginDao.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @BeforeEach
    void setUp() throws Exception {
        //Я сознательно не делал getter и setter поля loginDao в классе LoginController чтобы показать мощь рефлексии!
        Field loginDaoField = LoginController.class.getDeclaredField("loginDao");
        loginDaoField.setAccessible(true);
        loginDaoField.set(controller, loginDao);
    }

    @Test
    void doGetForwardsToLoginPage() throws ServletException, IOException {
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPostRedirectsToListWhenCredentialsAreValid() throws Exception {
        HttpSession session = mock(HttpSession.class);
        String rawPassword = "Passw0rd!";
        User user = User.builder()
                .id(1)
                .username("user")
                .password(BCrypt.hashpw(rawPassword, BCrypt.gensalt()))
                .build();

        when(request.getParameter("username")).thenReturn("user");
        when(request.getParameter("password")).thenReturn(rawPassword);
        when(loginDao.findByUsername(any())).thenReturn(user);
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/todo");

        controller.doPost(request, response);

        verify(session).setAttribute("user", user);
        verify(response).sendRedirect("/todo/list");
    }

    @Test
    void doPostForwardsBackWhenCredentialsAreInvalid() throws Exception {
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        User user = User.builder()
                .id(1)
                .username("user")
                .password(BCrypt.hashpw("Passw0rd!", BCrypt.gensalt()))
                .build();

        when(request.getParameter("username")).thenReturn("user");
        when(request.getParameter("password")).thenReturn("WrongPass1!");
        when(loginDao.findByUsername(any())).thenReturn(user);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        controller.doPost(request, response);

        verify(request).setAttribute("ERROR", "Invalid credentials");
        verify(dispatcher).forward(request, response);
        verify(request, never()).getSession();
    }

    @Test
    void doPostForwardsBackWhenUserIsNotFound() throws Exception {
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getParameter("username")).thenReturn("missing");
        when(request.getParameter("password")).thenReturn("Passw0rd!");
        when(loginDao.findByUsername(any())).thenReturn(null);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        controller.doPost(request, response);

        verify(request).setAttribute("ERROR", "Invalid credentials");
        verify(dispatcher).forward(request, response);
    }
}
