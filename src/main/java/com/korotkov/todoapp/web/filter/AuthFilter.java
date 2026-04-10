package com.korotkov.todoapp.web.filter;

import com.korotkov.todoapp.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        // Разрешаем доступ к login и logout и register и стилям
        if (path.startsWith("/login") ||
                path.startsWith("/register") ||
                path.startsWith("/css") ||
                path.startsWith("/js") ||
                path.startsWith("/images") ||
                path.equals("/favicon.ico") ||
                path.startsWith("/logout")) {

            chain.doFilter(req, res);
            return;
        }

        var session = request.getSession(false);//request.getSession(false); false - Чтобы не создавать пустую сессию
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // если пользователь есть — пускаем дальше в ToDoController
        chain.doFilter(req, res);
    }
}
