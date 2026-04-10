package com.korotkov.todoapp.validation;

import com.korotkov.todoapp.dao.UserDao;
import com.korotkov.todoapp.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserValidatorTest {

    private final UserDao userDao = mock(UserDao.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final UserValidator validator = new UserValidator(userDao);

    @Test
    void isValidReturnsTrueAndTrimsFieldsForCorrectInput() {
        when(request.getParameter("firstName")).thenReturn("  Ivan ");
        when(request.getParameter("lastName")).thenReturn(" Petrov  ");
        when(request.getParameter("username")).thenReturn(" test_user ");
        when(request.getParameter("password")).thenReturn(" Passw0rd! ");
        when(userDao.getUserByName("test_user")).thenReturn(null);

        boolean result = validator.isValid(request);

        assertTrue(result);
        assertEquals("Ivan", validator.getFirstName());
        assertEquals("Petrov", validator.getLastName());
        assertEquals("test_user", validator.getUsername());
        assertEquals("Passw0rd!", validator.getPassword());
        assertNull(validator.getErrorMessage());
    }

    @Test
    void isValidReturnsFalseWhenUsernameIsBlank() {
        when(request.getParameter("username")).thenReturn("   ");
        when(request.getParameter("password")).thenReturn("Passw0rd!");

        boolean result = validator.isValid(request);

        assertFalse(result);
        assertEquals("Имя пользователя не передано", validator.getErrorMessage());
        verify(userDao, never()).getUserByName(anyString());
    }

    @Test
    void isValidReturnsFalseWhenUsernameAlreadyExists() {
        when(request.getParameter("firstName")).thenReturn("Ivan");
        when(request.getParameter("lastName")).thenReturn("Petrov");
        when(request.getParameter("username")).thenReturn("existing_user");
        when(request.getParameter("password")).thenReturn("Passw0rd!");
        when(userDao.getUserByName("existing_user")).thenReturn(User.builder().id(1).username("existing_user").build());

        boolean result = validator.isValid(request);

        assertFalse(result);
        assertEquals("Пользователь уже существует", validator.getErrorMessage());
    }

    @Test
    void isValidReturnsFalseWhenPasswordDoesNotMatchPolicy() {
        when(request.getParameter("firstName")).thenReturn("Ivan");
        when(request.getParameter("lastName")).thenReturn("Petrov");
        when(request.getParameter("username")).thenReturn("new_user");
        when(request.getParameter("password")).thenReturn("weakpass");
        when(userDao.getUserByName("new_user")).thenReturn(null);

        boolean result = validator.isValid(request);

        assertFalse(result);
        assertNotNull(validator.getErrorMessage());
        assertTrue(validator.getErrorMessage().contains("Пароль должен содержать"));
    }
}
