package com.korotkov.todoapp.web;


import com.korotkov.todoapp.dao.UserDao;
import com.korotkov.todoapp.model.User;
import com.korotkov.todoapp.validation.UserValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*1. Пользователь вводит URL: /register → срабатывает doGet()
2. doGet() отдаёт register.jsp (через forward или redirect)
3. Пользователь заполняет форму → POST на /register → doPost()
4. doPost() сохраняет пользователя → делает forward обратно на register.jsp с уведомлением
*/

@WebServlet("/register")
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    public void init() {
        userDao = new UserDao();
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        register(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserValidator userValidator = new UserValidator(userDao);
        if (!userValidator.isValid(request)) {
            request.setAttribute("ERROR", userValidator.getErrorMessage());
            request.setAttribute("firstName", userValidator.getFirstName());
            request.setAttribute("lastName", userValidator.getLastName());
            request.setAttribute("username", userValidator.getUsername());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User user = User.builder()
                .firstName(userValidator.getFirstName())
                .lastName(userValidator.getLastName())
                .username(userValidator.getUsername())
                .password(userValidator.getPassword()).build();

        try {
            userDao.registerUser(user);
            // После регистрации автоматически логиним пользователя
            request.getSession().setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/list");
        } catch (Exception e) {
            request.setAttribute("ERROR", "Ошибка при регистрации");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}


