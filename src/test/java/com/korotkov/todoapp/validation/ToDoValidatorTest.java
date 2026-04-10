package com.korotkov.todoapp.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ToDoValidatorTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final ToDoValidator validator = new ToDoValidator();

    @Test
    void isValidReturnsTrueAndTrimsFieldsForCorrectInput() {
        when(request.getParameter("title")).thenReturn("  Buy milk ");
        when(request.getParameter("description")).thenReturn(" Call the store  ");

        boolean result = validator.isValid(request);

        assertTrue(result);
        assertEquals("Buy milk", validator.getTitle());
        assertEquals("Call the store", validator.getDescription());
        assertNull(validator.getErrorMessage());
    }

    @Test
    void isValidReturnsFalseWhenTitleIsMissing() {
        when(request.getParameter("title")).thenReturn(null);
        when(request.getParameter("description")).thenReturn("Task details");

        boolean result = validator.isValid(request);

        assertFalse(result);
        assertEquals("title не может быть пустым", validator.getErrorMessage());
    }

    @Test
    void isValidReturnsFalseWhenDescriptionIsBlank() {
        when(request.getParameter("title")).thenReturn("Task");
        when(request.getParameter("description")).thenReturn("   ");

        boolean result = validator.isValid(request);

        assertFalse(result);
        assertEquals("description не может быть пустым", validator.getErrorMessage());
    }

    @Test
    void isValidReturnsFalseWhenTitleIsTooLong() {
        when(request.getParameter("title")).thenReturn("a".repeat(101));
        when(request.getParameter("description")).thenReturn("Task details");

        boolean result = validator.isValid(request);

        assertFalse(result);
        assertEquals("title не может быть длиннее 100 символов", validator.getErrorMessage());
    }
}
