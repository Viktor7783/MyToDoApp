package com.korotkov.todoapp.web.filter;

import com.korotkov.todoapp.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class AuthFilterTest {

    private final AuthFilter filter = new AuthFilter();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    @Test
    void doFilterAllowsPublicPaths() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/login");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doFilterRedirectsGuestFromProtectedPath() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/list");
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/todo");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/todo/login");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void doFilterAllowsAuthorizedUser() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(request.getServletPath()).thenReturn("/list");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(User.builder().id(1).username("user").build());

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
